/**
 * TraMBU - an open time management tool
 *
 * Copyright (C) 2019  Stijn Dejongh
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 *
 * For further information on usage, or licensing, contact the author through his github profile:
 * https://github.com/justDoji
 */
package be.doji.productivity.trambu.front.controller.page;

import be.doji.productivity.trambu.front.calculator.TimeSpentCalculator;
import be.doji.productivity.trambu.front.controller.exception.InvalidReferenceException;
import be.doji.productivity.trambu.front.controller.state.ActivityModelContainer;
import be.doji.productivity.trambu.front.filter.FilterChain;
import be.doji.productivity.trambu.front.transfer.ActivityModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;

@SessionScope
@Named
public class ActivityOverviewController {

  private static final Logger LOG = LoggerFactory.getLogger(ActivityOverviewController.class);
  private final ActivityModelContainer activityContainer;
  private final FilterChain<ActivityModel> filterchain = new FilterChain<>();

  private boolean autotracking;

  @SuppressWarnings({"CdiInjectionPointsInspection"})
  @Inject
  ActivityOverviewController(@Autowired ActivityModelContainer activityContainer) {
    this.activityContainer = activityContainer;
  }

  public List<ActivityModel> getFilteredActivities() {
    return filterchain.getFilteredData(activityContainer.getActivities());
  }

  public void toggleEditable(ActivityModel model) {
    ActivityModel toToggle = activityContainer.getActivity(model.getReferenceKey());
    boolean editable = toToggle.isEditable();

    if (model.isEditable()) {
      activityContainer.saveActivities();
    }

    toToggle.setEditable(!editable);
  }

  public void toggleExpanded(ActivityModel model) {
    ActivityModel toToggle = activityContainer.getActivity(model.getReferenceKey());
    toToggle.setExpanded(!toToggle.isExpanded());

    if (isAutotracking()) {
      LOG.debug("Autotrack for activity: {}", model.getTitle());
      toggleTimelog(model);
    }
  }

  public void toggleCompleted(ActivityModel model) {
    ActivityModel toToggle = activityContainer.getActivity(model.getReferenceKey());
    toToggle.setCompleted(!toToggle.isCompleted());
    activityContainer.saveActivities();
  }

  public String createActivity() {
    return activityContainer.createActivity();
  }

  public void deleteActivity(ActivityModel toDelete) {
    try {
      activityContainer.deleteActivity(toDelete.getReferenceKey());
    } catch (InvalidReferenceException e) {
      String message = "An error occured while deleting an activity";
      LOG.error(message);
      showMessage(message);
    }
  }

  public List<String> completeTags(String query) {
    return getOptions(query, ActivityModel::getTags);
  }

  public List<String> completeProjects(String query) {
    return getOptions(query, ActivityModel::getProjects);
  }

  public List<String> getAllExistingTags() {
    return getValuesFor(ActivityModel::getTags).orElse(new ArrayList<>());
  }

  public List<String> getAllExistingProjects() {
    return getValuesFor(ActivityModel::getProjects).orElse(new ArrayList<>());
  }

  private List<String> getOptions(String query, Function<ActivityModel, List<String>> getter) {
    Optional<List<String>> reducedValues = getValuesFor(getter);

    List<String> options = reducedValues.orElse(new ArrayList<>());
    Set<String> returnOptions = new HashSet<>();
    for (String option : options) {
      if (option.toLowerCase().contains(query.toLowerCase())) {
        returnOptions.add(option);
      }
    }
    return new ArrayList<>(returnOptions);
  }

  private Optional<List<String>> getValuesFor(Function<ActivityModel, List<String>> getter) {
    Optional<List<String>> reduce = this.getFilteredActivities().stream().map(getter)
        .reduce(this::reduceStrings);
    reduce.ifPresent(strings -> strings.sort(String.CASE_INSENSITIVE_ORDER));
    return reduce;
  }

  private List<String> reduceStrings(List<String> strings, List<String> strings2) {
    Set<String> result = new HashSet<>();
    result.addAll(strings);
    result.addAll(strings2);
    return new ArrayList<>(result);
  }


  private void showMessage(String message) {
    FacesContext context = FacesContext.getCurrentInstance();

    if (context != null) {
      context.addMessage(null, new FacesMessage("Info", message));
    }
  }

  public void addTagFilter(String tagToInclude) {
    this.filterchain
        .addPositiveFiler(tagToInclude, ActivityModel::getTags,
            tags -> tags.contains(tagToInclude), "Tag");
  }

  public void addProjectFilter(String projectToInclude) {
    this.filterchain
        .addPositiveFiler(projectToInclude, ActivityModel::getProjects,
            projects -> projects.contains(projectToInclude), "Project");
  }

  public FilterChain<ActivityModel> getFilterchain() {
    return filterchain;
  }

  public void resetFilter() {
    this.filterchain.reset();
  }

  public void toggleTimelog(ActivityModel model) {
    ActivityModel toUpdate = activityContainer.getActivity(model.getReferenceKey());
    toUpdate.toggleTimeLog();
    activityContainer.saveActivities();
  }

  public boolean isAutotracking() {
    return autotracking;
  }

  public void setAutotracking(boolean autotracking) {
    LOG.debug("autotrack setter: {}", autotracking);
    this.autotracking = autotracking;
  }

  public void toggleAutotrack() {
    LOG.debug("Toggle autotrack");
    this.autotracking = !this.autotracking;
  }

  public String hoursSpentTotal(String referenceKey) {
    return TimeSpentCalculator.hoursSpentTotal(activityContainer.getActivity(referenceKey));
  }

  public String hoursSpentToday(String referenceKey) {
    return TimeSpentCalculator.hoursSpentToday(activityContainer.getActivity(referenceKey));
  }


}
