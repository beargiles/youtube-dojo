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

--
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--    http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

alter table playlist add constraint playlist_channel_key_channel_key_fkey foreign key (channel_key) references channel(key);

alter table channel_section add constraint channel_section_channel_key_channel_key_fkey foreign key (channel_key) references channel(key);
alter table channel_section add constraint channel_section_type_channel_section_type_fkey foreign key (type) references channel_section_type(type) on delete no action;

alter table tag add constraint tags_pk primary key(id);
alter table tag add constraint tags_original_uniq unique(original);

-- join tables

alter table channel_x_freebase_topic add constraint channel_x_freebase_topic_channel_key_fkey foreign key (channel_key) references channel(key) on delete no action;
alter table channel_x_freebase_topic add constraint channel_x_freebase_topic_topic_key_fkey foreign key (topic_key) references freebase_topic(key) on delete no action;

alter table channel_x_wikipedia_topic add constraint channel_x_wikipedia_topic_channel_key_fkey foreign key (channel_key) references channel(key) on delete no action;
alter table channel_x_wikipedia_topic add constraint channel_x_wikipedia_topic_wikipedia_url_fkey foreign key (wiki_key) references wikipedia_topic(key) on delete no action;

-- alter table playlist_item add constraint playlist_item_playlist_fkey foreign key (playlist_id) references playlist(id);
-- alter table playlist_item add constraint playlist_item_channel_fkey foreign key (channel_id) references channel(id);

-- alter table video add constraint video_channel_fkey foreign key (channel_id) references channel(id);

-- alter table channel_topic add constraint channel_topic_channel_fkey foreign key (channel_id) references channel(id);
-- alter table channel_topic add constraint channel_topic_topic_fkey foreign key (topic_id) references topic(id);

-- alter table video_topic add constraint video_topic_video_fkey foreign key (video_id) references video(id);
-- alter table video_topic add constraint video_topic_topic_fkey foreign key (topic_id) references topic(id);

-- alter table tags add constraint tags_video_fkey foreign key (video_id) references video(id);
-- alter table video_thumbnail add constraint video_thumbnail_video_fkey foreign key (video_id) references video(id);