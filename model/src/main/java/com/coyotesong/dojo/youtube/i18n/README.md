/*
 * Copyright (c) 2024 Bear Giles <bgiles@coyotesong.com>.
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Internationalization resources
 *
 * 'Language' and 'Region' contain the official ISO 639/BCP 47 and
 * ISO 3166-1 values, respectively, and are not specific to the YouTube API.
 *
 * They can be loaded from the classpath since they're defined by
 * international standards committees and will never be shown to the
 * user except for any pulldown menu to allow the user to select the
 * preferred language/region.
 *
 * This also means that we can load this information into the database
 * via 'flyway' initialization. We need it for referential integrity.
 *
 * Design note: we could use an 'enum' instead of a record, in both
 * the database and our code. This wouldn't save much space - in the
 * database it would replace a short string (+ 4 bytes) with an int.
 * However it would make it impossible for an attack to modify or
 * delete an existing value. With sufficient privileges they could add
 * new values.
 *
 * Implementation note: we can't initialize the values with ResourceBundles
 * since they're not UTF-8 safe. (!!!) Property files are key-value pairs
 * so they would still require a call to indexOf(). JSON and XML could
 * separate the values but they would add a lot of overhead. Finally the
 * data could be loaded from a prepopulated sqlite3 database but that
 * would require preparation and distribution of the file. So CSV, no
 * matter how ugly, is the best choice.
 *
 * Implementation note: some databases (e.g. postgresql) have a 'copy'
 * command but they require the file to be present on the database's
 * host system. Same problem as SQLite3 databases.
 *
 * Implementation note: since we control the data and know it won't
 * include anything tricky we don't need a full-function CSV parser,
 * much less a more advanced text format.
 */
package com.coyotesong.dojo.youtube.i18n;