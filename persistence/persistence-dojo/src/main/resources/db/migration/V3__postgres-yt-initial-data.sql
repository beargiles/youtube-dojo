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

-- copy channel_section_type(type) from '/tmp/channel_section_types.csv' with (FORMAT csv);
-- copy thumbnail_size(name, height, width) from '/tmp/thumbnail_sizes.csv' with (FORMAT csv);
-- copy freebase_topic(id, description, custom) from '/tmp/freebase_topics.csv' with (FORMAT csv);


-- load channel section types
--
-- todo: some types provide channels or playlists
insert into channel_section_type(type)
values ('allplaylists'),
       ('channelsectiontypeundefined'),
       ('completedevents'),
       ('liveevents'),
       ('multiplechannels'),  -- has title, has channels
       ('multipleplaylists'), -- has title, has playlists
       ('popularuploads'),
       ('recentuploads'),
       ('singleplaylist'),    -- does not have title, has playlist
       ('subscriptions'),
       ('upcomingevents');
--
-- load thumbnail sizes
--
-- todo: additional sizes for playlist? images?
insert into thumbnail_size(name, height, width)
values ('default', 90, 120),
       ('medium', 180, 320),
       ('high', 360, 480),
       ('standard', 480, 640),
       ('maxres', 720, 1280);
--
-- load 'freebase' topics from https://developers.google.com/youtube/v3/docs/search/list
--
insert into freebase_topic(id, description)
values ('/m/04rlf', 'Music (parent topic)'),
       ('/m/02mscn', 'Christian music'),
       ('/m/0ggq0m', 'Classical music'),
       ('/m/01lyv', 'Country'),
       ('/m/02lkt', 'Electronic music'),
       ('/m/0glt670', 'Hip hop music'),
       ('/m/05rwpb', 'Independent music'),
       ('/m/03_d0', 'Jazz'),
       ('/m/028sqc', 'Music of Asia'),
       ('/m/0g293', 'Music of Latin America'),
       ('/m/064t9', 'Pop music'),
       ('/m/06cqb', 'Reggae'),
       ('/m/06j6l', 'Rhythm and blues'),
       ('/m/06by7', 'Rock music'),
       ('/m/0gywn', 'Soul music'),
       -- Gaming topics
       ('/m/0bzvm2', 'Gaming (parent topic)'),
       ('/m/025zzc', 'Action game'),
       ('/m/02ntfj', 'Action-adventure game'),
       ('/m/0b1vjn', 'Casual game'),
       ('/m/02hygl', 'Music video game'),
       ('/m/04q1x3q', 'Puzzle video game'),
       ('/m/01sjng', 'Racing video game'),
       ('/m/0403l3g', 'Role-playing video game'),
       ('/m/021bp2', 'Simulation video game'),
       ('/m/022dc6', 'Sports game'),
       ('/m/03hf_rm', 'Strategy video game'),
       -- Sports topics
       ('/m/06ntj', 'Sports (parent topic)'),
       ('/m/0jm_', 'American football'),
       ('/m/018jz', 'Baseball'),
       ('/m/018w8', 'Basketball'),
       ('/m/01cgz', 'Boxing'),
       ('/m/09xp_', 'Cricket'),
       ('/m/02vx4', 'Football'),
       ('/m/037hz', 'Golf'),
       ('/m/03tmr', 'Ice hockey'),
       ('/m/01h7lh', 'Mixed martial arts'),
       ('/m/0410tth', 'Motorsport'),
       ('/m/07bs0', 'Tennis'),
       ('/m/07_53', 'Volleyball'),
       -- Entertainment topics
       ('/m/02jjt', 'Entertainment (parent topic)'),
       ('/m/09kqc', 'Humor'),
       ('/m/02vxn', 'Movies'),
       ('/m/05qjc', 'Performing arts'),
       ('/m/066wd', 'Professional wrestling'),
       ('/m/0f2f9', 'TV shows'),
       -- Lifestyle topics
       ('/m/019_rr', 'Lifestyle (parent topic)'),
       ('/m/032tl', 'Fashion'),
       ('/m/027x7n', 'Fitness'),
       ('/m/02wbm', 'Food'),
       ('/m/03glg', 'Hobby'),
       ('/m/068hy', 'Pets'),
       ('/m/041xxh', 'Physical attractiveness [Beauty]'),
       ('/m/07c1v', 'Technology'),
       ('/m/07bxq', 'Tourism'),
       ('/m/07yv9', 'Vehicles'),
       -- Society topics,
       ('/m/098wr', 'Society (parent topic)'),
       ('/m/09s1f', 'Business'),
       ('/m/0kt51', 'Health'),
       ('/m/01h6rj', 'Military'),
       ('/m/05qt0', 'Politics'),
       ('/m/06bvp', 'Religion'),
       -- Other topics'
       ('/m/01k8wb', 'Knowledge'),
       ('/g/120yrv6h', '(oddball)') -- seen in the field
;

--
-- load representative data
--
insert into wikipedia_topic(url, label)
values ('https://en.wikipedia.org/wiki/Action-adventure_game', 'Action-adventure game'),
       ('https://en.wikipedia.org/wiki/Action_game', 'Action game'),
       ('https://en.wikipedia.org/wiki/Boxing', 'Boxing'),
       ('https://en.wikipedia.org/wiki/Christian_music', 'Christian music'),
       ('https://en.wikipedia.org/wiki/Classical_music', 'Classical music'),
       ('https://en.wikipedia.org/wiki/Country_music', 'Country music'),
       ('https://en.wikipedia.org/wiki/Electronic_music', 'Electronic music'),
       ('https://en.wikipedia.org/wiki/Entertainment', 'Entertainment'),
       ('https://en.wikipedia.org/wiki/Fashion', 'Fashion'),
       ('https://en.wikipedia.org/wiki/Film', 'Film'),
       ('https://en.wikipedia.org/wiki/Food', 'Food'),
       ('https://en.wikipedia.org/wiki/Health', 'Health'),
       ('https://en.wikipedia.org/wiki/Hobby', 'Hobby'),
       ('https://en.wikipedia.org/wiki/Humour', 'Humour'),
       ('https://en.wikipedia.org/wiki/Independent_music', 'Independent music'),
       ('https://en.wikipedia.org/wiki/Knowledge', 'Knowledge'),
       ('https://en.wikipedia.org/wiki/Lifestyle_(sociology)', 'Lifestyle (sociology)'),
       ('https://en.wikipedia.org/wiki/Military', 'Military'),
       ('https://en.wikipedia.org/wiki/Motorsport', 'Motorsport'),
       ('https://en.wikipedia.org/wiki/Music', 'Music'),
       ('https://en.wikipedia.org/wiki/Performing_arts', 'Performing arts'),
       ('https://en.wikipedia.org/wiki/Pet', 'Pet'),
       ('https://en.wikipedia.org/wiki/Physical_fitness', 'Physical fitness'),
       ('https://en.wikipedia.org/wiki/Politics', 'Politics'),
       ('https://en.wikipedia.org/wiki/Pop_music', 'Pop music'),
       ('https://en.wikipedia.org/wiki/Professional_wrestling', 'Professional wrestling'),
       ('https://en.wikipedia.org/wiki/Religion', 'Religion'),
       ('https://en.wikipedia.org/wiki/Rhythm_and_blues', 'Rhythm and blues'),
       ('https://en.wikipedia.org/wiki/Rock_music', 'Rock music'),
       ('https://en.wikipedia.org/wiki/Role-playing_video_game', 'Role-playing video game'),
       ('https://en.wikipedia.org/wiki/Simulation_video_game', 'Simulation video game'),
       ('https://en.wikipedia.org/wiki/Society', 'Society'),
       ('https://en.wikipedia.org/wiki/Soul_music', 'Soul music'),
       ('https://en.wikipedia.org/wiki/Sport', 'Sport'),
       ('https://en.wikipedia.org/wiki/Strategy_video_game', 'Strategy video game'),
       ('https://en.wikipedia.org/wiki/Technology', 'Technology'),
       ('https://en.wikipedia.org/wiki/Television_program', 'Television program'),
       ('https://en.wikipedia.org/wiki/Tourism', 'Tourism'),
       ('https://en.wikipedia.org/wiki/Vehicle', 'Vehicle'),
       ('https://en.wikipedia.org/wiki/Video_game_culture', 'Video game culture')
;