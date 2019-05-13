/**
 * MIT License
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package be.doji.productivity.trambu.infrastructure.file;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.infrastructure.converter.ActivityConverter;
import be.doji.productivity.trambu.infrastructure.converter.ActivityDataConverter;
import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileLoader {

  private final ActivityDatabaseRepository activityDatabaseRepository;

  public FileLoader(@Autowired ActivityDatabaseRepository activityDatabaseRepository) {
    this.activityDatabaseRepository = activityDatabaseRepository;
  }

  public void loadTodoFileActivities(String todoFileLocation) throws IOException {
    Path path = Paths.get(todoFileLocation);
    loadTodoFileActivities(path);
  }

  public void loadTodoFileActivities(File file) throws IOException {
    loadTodoFileActivities(file.toPath());
  }

  private void loadTodoFileActivities(Path path) throws IOException {
    List<String> todoFileLines = Files.readAllLines(path);
    for (String line : todoFileLines) {
      Activity parsedActivity = ActivityConverter.parse(line);
      ActivityData convertedActivityData = ActivityDataConverter.parse(parsedActivity);
      activityDatabaseRepository.save(convertedActivityData);
    }
  }


}