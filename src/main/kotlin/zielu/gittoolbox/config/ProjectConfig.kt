package zielu.gittoolbox.config

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.annotations.Transient
import zielu.gittoolbox.metrics.ProjectMetrics
import zielu.gittoolbox.util.AppUtil
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@State(name = "GitToolBoxProjectSettings", storages = [Storage("git_toolbox_prj.xml")])
internal class ProjectConfig(
  @Transient
  private val project: Project
) : PersistentStateComponent<GitToolBoxConfigPrj> {
  private val lock = ReentrantLock()
  private var state: GitToolBoxConfigPrj = GitToolBoxConfigPrj()

  override fun getState(): GitToolBoxConfigPrj {
    lock.withLock {
      return state
    }
  }

  override fun loadState(state: GitToolBoxConfigPrj) {
    lock.withLock {
      log.debug("Project config state loaded: ", state)
      this.state = state
    }
  }

  override fun noStateLoaded() {
    log.info("No persisted state of project configuration")
  }

  override fun initializeComponent() {
    lock.withLock {
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
    lock.withLock {
      val current = state
      if (updated != current) {
        state = updated
        fireChanged(current, updated)
      }
    }
  }

  private fun fireChanged(previous: GitToolBoxConfigPrj, current: GitToolBoxConfigPrj) {
    project.messageBus.syncPublisher(ProjectConfigNotifier.CONFIG_TOPIC).configChanged(previous, current)
  }

  companion object {
    private val log = Logger.getInstance(ProjectConfig::class.java)

    @JvmStatic
    fun get(project: Project): GitToolBoxConfigPrj {
      return getInstance(project).getState()
    }

    @JvmStatic
    fun getInstance(project: Project): ProjectConfig {
      return AppUtil.getServiceInstance(project, ProjectConfig::class.java)
    }
  }
}
