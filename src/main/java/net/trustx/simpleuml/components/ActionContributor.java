package net.trustx.simpleuml.components;

public interface ActionContributor {
  ActionContributorCommandInfo[] getActionContributorCommandInfos();
  
  ActionContributorCommand getActionContributorCommand(ActionContributorCommandInfo paramActionContributorCommandInfo);
}


