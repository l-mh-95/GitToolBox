package zielu.gittoolbox.ui.config;

import com.intellij.ui.ListCellRendererWrapper;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import zielu.gittoolbox.ui.StatusPresenter;
import zielu.gittoolbox.ui.StatusPresenters;

public class GitToolBoxForm {
    private JComboBox presentationMode;
    private JPanel content;
    private JCheckBox showGitStatCheckBox;
    private JCheckBox showProjectViewStatusCheckBox;

    public void init() {
        presentationMode.setRenderer(new ListCellRendererWrapper<StatusPresenter>() {
            @Override
            public void customize(JList jList, StatusPresenter presenter, int index,
                                  boolean isSelected, boolean hasFocus) {
                setText(presenter.getLabel());
            }
        });
        presentationMode.setModel(new DefaultComboBoxModel(StatusPresenters.values()));
    }

    public JComponent getContent() {
        return content;
    }

    public void setPresenter(StatusPresenter presenter) {
        presentationMode.setSelectedItem(presenter);
    }

    public StatusPresenter getPresenter() {
        return (StatusPresenter) presentationMode.getSelectedItem();
    }

    public void setShowGitStatus(boolean showGitStatus) {
        showGitStatCheckBox.setSelected(showGitStatus);
    }

    public boolean getShowGitStatus() {
        return showGitStatCheckBox.isSelected();
    }

    public void setShowProjectViewStatus(boolean showProjectViewStatus) {
        showProjectViewStatusCheckBox.setSelected(showProjectViewStatus);
    }

    public boolean getShowProjectViewStatus() {
        return showProjectViewStatusCheckBox.isSelected();
    }
}