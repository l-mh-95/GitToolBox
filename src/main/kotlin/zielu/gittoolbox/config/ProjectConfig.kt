package zielu.gittoolbox.config

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.annotations.Transient
import zielu.gittoolbox.metrics.ProjectMetrics
import zielu.gittoolbox.util.AppUtil

@State(name = "GitToolBoxProjectSettings", storages = [Storage("git_toolbox_prj.xml")])
internal class ProjectConfig(
  @Transient
  private val project: Project
) : PersistentStateComponent<GitToolBoxConfigPrj> {
  private var state: GitToolBoxConfigPrj = GitToolBoxConfigPrj()

  override fun getState(): GitToolBoxConfigPrj {
    synchronized(this) {
      return state
    }
  }

  override fun loadState(state: GitToolBoxConfigPrj) {
    synchronized(this) {
      log.debug("Project config state loaded: ", state)
      this.state = state
    }
  }

  override fun noStateLoaded() {
    log.info("No persisted state of project configuration")
  }

  override fun initializeComponent() {
    synchronized(this) {
      migrate()
    }
  }

  private fun migrate() {
    val appConfig = AppConfig.getConfig()
    val timer = ProjectMetrics.getInstance(project).timer("project-config.migrate")
    val result = timer.timeSupplierKt { ConfigMigrator().migrate(project, state, appConfig) }
    if (result) {
      log.info("Migration done")
    } else {
      log.info("Already migrated")
    }
  }

  fun updateState(updated: GitToolBoxConfigPrj) {
    var fire = false
    var current: GitToolBoxConfigPrj
    synchronized(this) {
      current = state
      if (updated != current) {
        state = updated
        fire = true
      }
    }
    if (fire) {
      fireChanged(current, updated)
    }
  }

  private fun fireChanged(previous: GitToolBoxConfigPrj, current: GitToolBoxConfigPrj) {
    project.messageBus.syncPublisher(ProjectConfigNotifier.CONFIG_TOPIC).configChanged(previous, current)
  }

  companion object {
    private val log = Logger.getInstance(ProjectConfig::class.java)

    @JvmStatic
    fun getConfig(project: Project): GitToolBoxConfigPrj {
      return getInstance(project).state
    }

    @JvmStatic
    fun getMerged(project: Project): MergedProjectConfig {
      return MergedProjectConfig(AppConfig.getConfig(), getConfig(project))
    }

    @JvmStatic
    fun getInstance(project: Project): ProjectConfig {
      return AppUtil.getServiceInstance(project, ProjectConfig::class.java)
    }
  }
}
