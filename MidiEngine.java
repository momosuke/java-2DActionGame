import java.io.IOException;
import java.net.URL;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/**
 * Midi�����̍Đ��p�G���W��
 * @author momosuke
 * @version 1.0
 */
public class MidiEngine {
    // �o�^�ł���MIDI�t�@�C���̍ő吔
    private static final int MAX_SEQUENCE = 256;
    // MIDI���^�C�x���g
    private static final int END_OF_TRACK_MESSAGE = 47;

    // MIDI�V�[�P���X
    private static Sequence[] sequences = new Sequence[MAX_SEQUENCE];
    // MIDI�V�[�P���T
    private static Sequencer sequencer;

    // �o�^���ꂽMIDI�t�@�C���̐�
    private static int counter = 0;

    // �Đ�����MIDI�V�[�P���X�̓o�^�ԍ�
    private static int playSequenceNo = -1;

    // MIDI�V�[�P���X�̊J�n�n�_
    private static long startTick = 0;

    /**
     * MIDI�t�@�C�������[�h
     * @param url MIDI�t�@�C����URL
     */
    public static void load(URL url) throws MidiUnavailableException, InvalidMidiDataException, IOException {
        if (sequencer == null) {
            // �V�[�P���T���擾
            sequencer = MidiSystem.getSequencer();
            // �V�[�P���T���J��
            sequencer.open();
            // ���^�C�x���g���X�i�[��o�^
            sequencer.addMetaEventListener(new MyMetaEventListener());
        }

        // MIDI�V�[�P���X��o�^
        sequences[counter] = MidiSystem.getSequence(url);

        counter++;
    }
    
    /**
     * MIDI�t�@�C�������[�h
     * @param filename MIDI�t�@�C����
     */
    public static void load(String filename) throws MidiUnavailableException, InvalidMidiDataException, IOException {
        URL url = MidiEngine.class.getResource(filename);
        load(url);
    }

    /**
     * �Đ�
     * @param no �o�^�ԍ�
     */
    public static void play(int no) {
        // �o�^����ĂȂ���Ή������Ȃ�
        if (sequences[no] == null) {
            return;
        }
        
        // ���ݍĐ�����MIDI�t�@�C���Ɠ����ꍇ�͉������Ȃ�
        //if (playSequenceNo == no) {
            //return;
        //}

        // �ʂ�MIDI�V�[�P���X���Đ�����ꍇ��
        // ���ݍĐ����̃V�[�P���X���~����
        stop();

        try {
            // �V�[�P���T��MIDI�V�[�P���X���Z�b�g
            sequencer.setSequence(sequences[no]);
            // �o�^�ԍ����L��
            playSequenceNo = no;
            // MIDI�V�[�P���T�̃X�^�[�g�n�_���L�^�i���[�v�ł���悤�Ɂj
            startTick = sequencer.getMicrosecondPosition();
            // �Đ��J�n
            sequencer.start();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * ���f
     */
    public static void stop() {
        if (sequencer.isRunning()) {
            sequencer.stop();
        }
    }
    
    /**
     * �ĊJ
     */
    public static void start(int no) {
        try {
            // �V�[�P���T��MIDI�V�[�P���X���Z�b�g
            sequencer.setSequence(sequences[no]);
            // �Đ��J�n
            sequencer.start();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * ��~
     */
    public static void close() {
        if (sequencer.isRunning()) {
	    sequencer.setMicrosecondPosition(startTick);
            sequencer.stop();
        }
    }

    /**
     * ���[�v�Đ�
     */
    private static class MyMetaEventListener implements MetaEventListener {
        public void meta(MetaMessage meta) {
            if (meta.getType() == END_OF_TRACK_MESSAGE) {
                if (sequencer != null && sequencer.isOpen()) {
                    // MIDI�V�[�P���X�Đ��ʒu���ŏ��ɖ߂�
                    sequencer.setMicrosecondPosition(startTick);
                    // �ŏ�����Đ�
                    sequencer.start();
                }
            }
        } 
    }
}