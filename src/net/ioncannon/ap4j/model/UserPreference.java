package net.ioncannon.ap4j.model;

import java.util.prefs.Preferences;
import java.util.prefs.BackingStoreException;
import java.util.List;
import java.util.ArrayList;

/**
 * Copyright (c) 2011 Carson McDonald
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

public class UserPreference
{
  private static final String LOCAL_DIRECTORY_COUNT = "local_dir_count";
  private static final String LOCAL_DIRECTORY = "local_dir_";

  public static void addLocalDirectory(String directoryName)
  {
    Preferences preferences = Preferences.userNodeForPackage(UserPreference.class);

    int dirCount = preferences.getInt(LOCAL_DIRECTORY_COUNT, 0);
    preferences.put(LOCAL_DIRECTORY + String.valueOf(dirCount), directoryName);
    preferences.putInt(LOCAL_DIRECTORY_COUNT, dirCount+1);

    try
    {
      preferences.flush();
    }
    catch (BackingStoreException e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  public static List<String> getLocalDirectories()
  {
    Preferences preferences = Preferences.userNodeForPackage(UserPreference.class);

    List<String> localDirectories = new ArrayList<String>();

    int dirCount = preferences.getInt(LOCAL_DIRECTORY_COUNT, 0);
    for(int i=0; i<dirCount; i++)
    {
      localDirectories.add(preferences.get(LOCAL_DIRECTORY + String.valueOf(i), null));
    }

    return localDirectories;
  }

  public static void removeLocalDirectory(String directoryName)
  {
    Preferences preferences = Preferences.userNodeForPackage(UserPreference.class);

    int dirCount = preferences.getInt(LOCAL_DIRECTORY_COUNT, 0);
    for(int i=0; i<dirCount; i++)
    {
      if(directoryName.equals(preferences.get(LOCAL_DIRECTORY + String.valueOf(i), null)))
      {
        preferences.remove(LOCAL_DIRECTORY + String.valueOf(i));
        break;
      }
    }

    try
    {
      preferences.flush();
    }
    catch (BackingStoreException e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
