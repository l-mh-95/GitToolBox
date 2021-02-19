package zielu.gittoolbox.config

internal class MergedProjectConfig(
  private val appConfig: GitToolBoxConfig2,
  private val projectConfig: GitToolBoxConfigPrj
) {

  fun autoFetchEnabled(): Boolean {
    return if (projectConfig.autoFetchEnabledOverride.enabled) {
      projectConfig.autoFetchEnabledOverride.value
    } else {
      appConfig.autoFetchEnabled
    }
  }

  fun autoFetchIntervalMinutes(): Int {
    return if (projectConfig.autoFetchIntervalMinutesOverride.enabled) {
      projectConfig.autoFetchIntervalMinutesOverride.value
    } else {
      appConfig.autoFetchIntervalMinutes
    }
  }

  fun autoFetchOnBranchSwitch(): Boolean {
    return if (projectConfig.autoFetchOnBranchSwitchOverride.enabled) {
      projectConfig.autoFetchOnBranchSwitchOverride.value
    } else {
      appConfig.autoFetchOnBranchSwitch
    }
  }

  fun commitDialogBranchCompletion(): Boolean {
    return if (projectConfig.commitDialogBranchCompletionOverride.enabled) {
      projectConfig.commitDialogBranchCompletionOverride.value
    } else {
      appConfig.commitDialogCompletion
    }
  }

  fun commitDialogGitmojiCompletion(): Boolean {
    return if (projectConfig.commitDialogGitmojiCompletionOverride.enabled) {
      projectConfig.commitDialogGitmojiCompletionOverride.value
    } else {
      appConfig.commitDialogGitmojiCompletion
    }
  }

  fun commitDialogCompletionConfigs(): List<CommitCompletionConfig> {
    return if (projectConfig.completionConfigsOverride.enabled) {
      projectConfig.completionConfigsOverride.values
    } else {
      appConfig.completionConfigs
    }
  }

  fun referencePointForStatus(): ReferencePointForStatusConfig {
    return if (projectConfig.referencePointForStatusOverride.enabled) {
      projectConfig.referencePointForStatusOverride.value
    } else {
      appConfig.referencePointForStatus
    }
  }

  fun commitMessageValidation(): Boolean {
    return if (projectConfig.commitMessageValidationOverride.enabled) {
      projectConfig.commitMessageValidationOverride.value
    } else {
      appConfig.commitMessageValidationEnabled
    }
  }

  fun commitMessageValidationRegex(): String {
    return if (projectConfig.commitMessageValidationRegexOverride.enabled) {
      projectConfig.commitMessageValidationRegexOverride.value
    } else {
      appConfig.commitMessageValidationRegex
    }
  }
}
