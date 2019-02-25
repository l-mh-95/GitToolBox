package zielu.gittoolbox.ui.blame;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.LineExtensionInfo;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import java.util.Collection;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zielu.gittoolbox.config.GitToolBoxConfig2;
import zielu.gittoolbox.util.AppUtil;

interface BlameEditorService {
  @Nullable
  Collection<LineExtensionInfo> getLineExtensions(@NotNull VirtualFile file, int editorLineNumber);

  void colorsSchemeChanged(@NotNull EditorColorsScheme colorsScheme);

  void configChanged(@NotNull GitToolBoxConfig2 config);

  @NotNull
  static BlameEditorService getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, BlameEditorService.class);
  }

  @NotNull
  static Optional<BlameEditorService> getExistingInstance(@NotNull Project project) {
    return AppUtil.getExistingServiceInstance(project, BlameEditorService.class);
  }
}
