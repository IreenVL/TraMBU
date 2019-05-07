/**
 * MIT License
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package be.doji.productivity.trambu.infrastructure.parser;

public final class PropertyIndicator {

  /*Utility classes should not have a public or default constructor */
  private PropertyIndicator() {
  }

  static final String INDICATOR_DONE = "x";
  static final String INDICATOR_DONE_UPPERCASE = "X";
  static final String INDICATOR_PROJECT = "+";
  static final String INDICATOR_TAG = "@";
  static final String INDICATOR_DEADLINE = "due:";
  static final String INDICATOR_WARNING_PERIOD = "warningPeriod:";
  static final String INDICATOR_PARENT_ACTIVITY = "super:";
  static final String INDICATOR_UUID = "uuid:";
  static final String INDICATOR_LOCATION = "loc:";

  static final String INDICATOR_LOG_START = "LOG_START";
  static final String INDICATOR_LOG_END = "LOG_END";
  static final String INDICATOR_LOGPOINT_START = "STARTTIME:";
  static final String INDICATOR_LOGPOINT_END = "ENDTIME:";

}