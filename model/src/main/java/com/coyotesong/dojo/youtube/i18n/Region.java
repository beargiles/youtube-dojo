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

package com.coyotesong.dojo.youtube.i18n;

/**
 * I18N Region
 *
 * The 'hl' field is essentially the name of the locale but I want to avoid confusion
 * with the Locale class since it includes additional information.
 *
 * For more information see:
 *
 * - [YouTube API](https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/model/I18nRegion.html)
 */
@SuppressWarnings("unused")
public record Region (String code, String hl, String name, String country) {
}