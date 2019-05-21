/**
 * GNU Affero General Public License Version 3
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package be.doji.productivity.trambu.infrastructure.repository;

import static org.assertj.core.api.Java6Assertions.assertThat;

import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityTagData;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityDatabaseRepositoryTest {

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  ActivityDatabaseRepository repository;

  @Test
  public void repository_CanBeAutowired() {
    Assertions.assertThat(repository).isNotNull();
    Assertions.assertThat(repository.findAll()).hasSize(0);
  }

  @Test
  public void repository_saveEntity() {
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Save me");
    activityData.setCompleted(true);

    ActivityData saved = repository.save(activityData);

    Assertions.assertThat(repository.findById(saved.getId())).isPresent();

    ActivityData savedActivity = repository.findById(saved.getId()).get();
    Assertions.assertThat(savedActivity.getTitle()).isEqualTo("Save me");
    Assertions.assertThat(savedActivity.isCompleted()).isTrue();
  }

  @Test
  public void addActivity_withTags() {
    ActivityData activityData = new ActivityData();

    List<ActivityTagData> tagList = new ArrayList<>();
    tagList.add(new ActivityTagData("TestTag", activityData));
    tagList.add(new ActivityTagData("TestTagTwo", activityData));

    activityData.setTitle("Save me");
    activityData.setCompleted(true);
    activityData.setTags(tagList);

    assertThat(repository.findAll()).isEmpty();

    repository.save(activityData);

    assertThat(repository.findAll()).hasSize(1);
    assertThat(repository.findAll().get(0).getTitle()).isEqualTo("Save me");
    assertThat(repository.findAll().get(0).getTags()).isNotEmpty();
    assertThat(repository.findAll().get(0).getTags()).hasSize(2);
  }

  @After
  public void cleanUp() {
    repository.deleteAll();
  }


}