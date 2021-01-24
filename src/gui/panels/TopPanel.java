package gui.panels;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.Consts;

public class TopPanel extends JPanel {

  private static final long serialVersionUID = -4696796014681020374L;

  public InstallDirPanel installDirPanel;
  public SeedPanel seedPanel;

  public TopPanel(String initialInstallDir, long initialSeed) {
    this.installDirPanel = new InstallDirPanel(initialInstallDir);
    this.seedPanel = new SeedPanel(initialSeed);

    this.add(installDirPanel);
    this.add(seedPanel);
  }

  public static class InstallDirPanel extends JPanel {

    private static final long serialVersionUID = -496739146369168245L;

    JFileChooser fileChooser;
    private JLabel currentFile;

    public InstallDirPanel(String initialInstallDir) {
      fileChooser = new JFileChooser(initialInstallDir);
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      fileChooser.setMultiSelectionEnabled(false);
      JLabel currentFileLabel = new JLabel(Consts.CURRENT_FILE_LABEL);
      currentFileLabel.setToolTipText(Consts.CURRENT_FILE_TTT);

      currentFile = new JLabel(initialInstallDir);
      currentFile.setBorder(BorderFactory.createLoweredBevelBorder());
      JButton changeInstall = new JButton(Consts.CHANGE_CURRENT_FILE_BUTTON_LABEL);
      changeInstall.setActionCommand("changeInstallDir");
      changeInstall.addActionListener(new OnChangeDirClick(fileChooser, currentFile));

      this.setLayout(new FlowLayout());
      this.setAlignmentX(Component.RIGHT_ALIGNMENT);
      this.add(currentFileLabel);
      this.add(currentFile);
      this.add(changeInstall);
      this.setBorder(BorderFactory.createEtchedBorder());
    }
    
    public String getInstallDir() {
      return currentFile.getText();
    }
  }

  private static class OnChangeDirClick implements ActionListener {
    JFileChooser fileChooser;
    JLabel currentFile;

    public OnChangeDirClick(JFileChooser fileChooser, JLabel currentFile) {
      this.fileChooser = fileChooser;
      this.currentFile = currentFile;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      fileChooser.showOpenDialog(null);
      String installDir = Consts.INSTALL_DIR_HACKY_ERROR_MSG;
      try {
        installDir = fileChooser.getSelectedFile().getCanonicalPath();
        currentFile.setText(installDir);
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
  }

  public static class SeedPanel extends JPanel {

    private static final int SEED_TEXT_FIELD_LENGTH = 15;
    private static final long serialVersionUID = 7053547768520931743L;

    private JTextField seedField;

    public SeedPanel(long initialSeed) {
      JLabel seedLabel = new JLabel(Consts.CURRENT_SEED_LABEL);
      seedField = new JTextField(SEED_TEXT_FIELD_LENGTH);
      seedField.setHorizontalAlignment(JLabel.RIGHT);
      setSeedField(initialSeed);

      JButton newSeedButton = new JButton(Consts.NEW_SEED_LABEL);
      newSeedButton.setToolTipText(Consts.NEW_SEED_TTT);
      newSeedButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
          setSeedField(getNewSeed());
        }
      });
      this.add(seedLabel);
      this.add(seedField);
      this.add(newSeedButton);
      this.setBorder(BorderFactory.createEtchedBorder());
    }

    public static long getNewSeed() {
      return new Random().nextLong();
    }
    
    public long getSeed() {
      return Long.parseLong(seedField.getText());
    }

    public void setSeedField(long newSeed) {
      seedField.setText(Long.toString(newSeed));
    }
  }

}
