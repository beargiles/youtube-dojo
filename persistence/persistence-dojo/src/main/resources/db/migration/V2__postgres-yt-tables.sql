--
-- Copyright (c) 2024 Bear Giles <bgiles@coyotesong.com>.
-- All Rights Reserved.
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

--
-- Create tables
--

-- ************************************************************
-- Internationalization. (Should be moved into separate schema?)
-- ************************************************************
create table i18n_language
(
    key  serial not null,
    code text   not null,
    hl   text   not null,
    name text   not null,

    -- etag        text not null,
    -- parent_etag text not null,

    constraint i18n_language_pkey primary key (key)
);

create table i18n_region
(
    key  serial not null,
    code text   not null,
    hl   text   not null,
    name text   not null,
    gl   text,

    -- etag        text not null,
    -- parent_etag text not null,

    constraint i18n_region_pkey primary key (key)
);

-- note: the 'channel_id' is always "UCBR8-60-B28hp2BmDPdntcQ" (AFAIK)
create table video_category
(
    id          serial not null,
    category_id int4   not null,
    lang        text,

    title       text,
    assignable  boolean,

    etag        text   not null,
    parent_etag text   not null,

    constraint video_category_pkey primary key (id)
    -- constraint video_category_etag_key unique(etag),
    -- constraint video_category_parent_etag_category_id_key unique(parent_etag, category_id)
    -- constraint video_category_etag_key unique(etag)
);

-- ************************************************************
-- YouTube API constants used in referential integrity
-- ************************************************************
create type cst as enum (
    'allplaylists',
    'channelsectiontypeundefined',
    'completedevents',
    'liveevents',
    'multiplechannels',
    'multipleplaylists',
    'popularuploads',
    'recentuploads',
    'singleplaylist',
    'subscriptions',
    'upcomingevents'
    );

create table channel_section_type
(
    type text not null,

    constraint channel_section_type_pkey primary key (type)
);

-- ************************************************************
-- Pre-initialized constants
-- ************************************************************

-- aka 'topic category'
create table wikipedia_topic
(
    key    serial  not null,
    url    text    not null,
    label  text    not null,
    custom boolean not null default true,

    constraint wikipedia_topic_pkey primary key (key),
    constraint wikipedia_topic_url_unique unique (url)
);

create table freebase_topic
(
    key         serial not null,
    id          text   not null,
    description text   not null,

    constraint freebase_topic_pkey primary key (key)
);

-- ************************************************************
-- ************************************************************

create table channel_etag
(
    key          int  not null,
    etag         text not null,
    last_checked timestamp(0) without time zone,

    constraint channel_etag_pkey primary key (key)
);

--
-- Channel
--
create table channel
(
    key              serial  not null,
    channel_id       text    not null,
    handle           text    not null, -- e.g., @mrballen
    summary          boolean,

    title            text,
    description      text,
    content_owner    text,             -- always null ?
    uploads          text    not null, -- always 'playlist_id' ?
    published_at     timestamp(0) without time zone,
    video_count      int8,
    subscriber_count int8,
    view_count       int8,
    tn_url           text,             -- https://yt3.ggpht.com/...
    country          text,
    lang             text,

    -- etag             text not null,
    -- parent_etag      text,          -- used when channel identified via search

    nsfw             boolean,
    -- last_checked     timestamp(0) without time zone,

    constraint channel_pkey primary key (key),
    constraint channel_id_u unique (channel_id)
    -- constraint channel_etag_key unique(etag)
);

--
-- Playlist
--
create table playlist
(
    key             serial  not null,
    channel_key     int4    not null,

    id              text    not null,
    channel_id      text    not null,
    position        int4,
    summary         boolean,

    title           text,
    description     text,
    item_count      int8,
    -- channel_title   text,
    published_at    timestamp(0) without time zone,
    tn_default_url  text, -- https://i.ytimg.com/vi/...
    tn_medium_url   text, -- https://i.ytimg.com/vi/...
    tn_high_url     text, -- https://i.ytimg.com/vi/...
    tn_standard_url text, -- https://i.ytimg.com/vi/...
    tn_max_res_url  text, -- https://i.ytimg.com/vi/...
    tn_video_id     text,
    lang            text,
    hl              text,

    etag            text    not null,

    last_checked    timestamp(0) without time zone,

    constraint playlist_pkey primary key (key)
    -- constraint playlist_etag_key unique(etag)
    -- constraint playlist_channel_fkey foreign key (channel_id) references channel(id)
);

--
-- Individual item on Youtube Playlist
--
create table playlist_item
(
    id                 text not null,
    playlist_id        text not null,
    channel_id         text not null, -- redundant since playlist has channel_id ?
    video_id           text,
    title              text,
    description        text,          -- may be null
    note               text,          -- always null ?
    tn_url             text,          -- https://i.ytimg.com/vi/...
    owner_channel_id   text,          -- ever different from channel_id?
    published_at       timestamp(0) without time zone,
    video_published_at timestamp(0) without time zone,
    position           int4,
    kind               text,          -- always 'youtube#video' ?

    etag               text not null,

    constraint playlist_item_pkey primary key (id)
    -- constraint etag_key unique(etag)
    -- constraint playlist_item_playlist_fkey foreign key (playlist_id) references playlist(id)
    -- constraint playlist_item_channel_fkey foreign key (channel_id) references channel(id)
);

--
-- Youtube Video
--
create table video
(
    id                  text    not null,
    channel_id          text    not null,
    category_id         int4,
    title               text,
    description         text,
    lang                text,
    published_at        timestamp(0) without time zone,
    channel_title       text,
    embed_src           text,
    embeddable          boolean,
    license             text,
    comments            int8,
    likes               int8,
    views               int8,
    caption             boolean,
    content_rating      text,
    definition          text,
    dimension           text,
    duration            text,
    licensed            boolean,
    projection          text,
    region_restrictions text,
    mpaa                text,
    mpaat               text,
    tvpg                text,
    yt_rating           text,

    -- topic_ids           text[],
    -- topic_categories    int[],
    -- relevant_topics_ids text[],

    summary             boolean not null default false,
    etag                text    not null,

    last_checked        timestamp(0) without time zone,

    constraint video_pkey primary key (id)
);


create type tns as enum (
    'DEFAULT',
    'MEDIUM',
    'HIGH',
    'STANDARD',
    'MAX_RES'
    );

-- mostly for reference...
create table thumbnail_size
(
    name   text not null,
    height int4 not null,
    width  int4 not null,
    constraint thumbnail_size_pkey primary key (name)
);

create table thumbnail
(
    key      serial not null,
    name     text   not null,
    url      text   not null,
    contents bytea,
    -- height
    -- width

    constraint thumbnail_pk primary key (key)
);

create table channel_thumbnail
(
    channel_key int not null,
    constraint channel_thumbnail_pk primary key (channel_key)
) inherits (thumbnail);

create table playlist_thumbnail
(
    playlist_key int not null,
    constraint playlist_thumbnail_pk primary key (playlist_key)
) inherits (thumbnail);

create table playlist_item_thumbnail
(
    playlist_item_id int not null,
    constraint playlist_item_thumbnail_pk primary key (playlist_item_id)
) inherits (thumbnail);

create table video_thumbnail
(
    video_id int not null,
    constraint video_thumbnail_pk primary key (video_id)
) inherits (thumbnail);

create table tag
(
    id       serial not null,
    tag      text   not null,
    original text   not null
);

create table channel_section
(
    key         serial not null,
    channel_key int4   not null,

    section_id  text   not null,
    channel_id  text   not null,

    position    int4   not null,
    type        text,
    title       text,
    lang        text,
    hl          text,
    style       text,

    constraint channel_section_pkey primary key (key)
);

-- cross-reference table
create table channel_x_wikipedia_topic
(
    channel_key int4 not null,
    wiki_key    int4 not null,

    etag        text not null,

    constraint channel_x_wikipedia_topic_pkey primary key (channel_key, wiki_key)
);

-- cross-reference table
create table channel_x_freebase_topic
(
    channel_key int4 not null,
    topic_key   int4 not null,

    etag        text not null,

    constraint channel_x_freebase_topic_pkey primary key (channel_key, topic_key)
);

-- embedded list
create table channel_section_x_channel
(
    parent_channel_key int4 not null,
    parent_position    int4 not null,
    position           int4 not null,

    channel_id         text  not null,

    etag               text not null,

    constraint channel_section_x_channel_pkey primary key (parent_channel_key, parent_position, position)
);

-- embedded list
create table channel_section_x_playlist
(
    parent_channel_key int4 not null,
    parent_position    int4 not null,
    position           int4 not null,

    playlist_id        text  not null,

    etag               text not null,

    constraint channel_section_x_playlist_pkey primary key (parent_channel_key, parent_position, position)
);


-- cross-reference table
create table video_categories
(
    video_id    text not null,
    category_id int4

    -- constraint channel_topic_pkey primary key(channel_id, topic_id)
);

-- cross-reference table
create table video_tags
(
    video_id text not null,
    tag_id   int4 not null

    -- constraint video_tag_pkey primary key(video_id, tag_id)
);

-- cross-reference table
create table video_topics
(
    video_id text not null,
    topic_id int4

    -- constraint video_topic_pkey primary key(video_id, topic_id)
);
-- cross-reference table
create table video_x_wikipedia_topic
(
    video_key int4 not null,
    wiki_key  int4 not null,

    etag      text not null,

    constraint video_x_wikipedia_topic_pkey primary key (video_key, wiki_key)
);

-- cross-reference table
create table video_x_freebase_topic
(
    video_key int4 not null,
    topic_key int4 not null,

    etag      text not null,

    constraint video_x_freebase_topic_pkey primary key (video_key, topic_key)
);

--
-- Local extensions
--
create table subscription
(
    channel_id text not null
);

--
-- Used when importing history from Chrome browser
--
create table chrome_history
(
    video_id        text not null,
    title           text,
    visit_count     int4 not null,
    typed_count     int4 not null,
    last_visit_time timestamp(3) without time zone
);

--
-- Used to capture search parameters for multiple replays
--
create table search_request
(
    id                text not null,
    type              text not null,                  -- channel, playlist, video
    query             text,

    channel_id        text,
    channel_type      text,                           -- any, show
    event_type        text,                           -- complete, live, upcoming
    "order"           text,                           -- relevance; date, rating, title, videoCont, viewCount
    published_after   timestamp(0) without time zone, -- (RFC-3339)
    published_before  timestamp(0) without time zone,
    region_code       text,                           -- ISO 3166-1 alpha-2
    lang              text,                           -- ISO 691-1 two-letter language code
    safe_search       text,                           -- moderate; none, strict
    topic_id          text,                           -- freebase id
    username          text,

    video_caption     text,                           -- any, none, closedCaption
    video_category_id text,
    video_definition  text,                           -- any, high, standard
    video_dimension   text,                           -- 2d, 3d, any
    video_duration    text,                           -- any; short, medium, long
    video_embeddable  boolean,                        -- any, true
    video_license     text,                           -- any, youtube, creativeCommon
    video_syndicated  text,                           -- any, true
    video_type        text,                           -- any; episode, movie

    max_results       int,
    actual_results    int,
    last_checked      timestamp(0) without time zone,

    constraint search_request_pkey primary key (id)
);

--
-- Used to capture search results for multiple replays
--
create table search_result
(
    id                     serial not null,
    request_id             text   not null,
    position               int    not null,

    channel_id             text,
    playlist_id            text,
    video_id               text,
    channel_title          text,
    title                  text,
    description            text,
    live_broadcast_content text,
    published_at           timestamp(0) without time zone,
    tn_url                 text,

    etag                   text   not null,
    parent_etag            text   not null,
    last_checked           timestamp(0) without time zone,

    constraint search_results_pkey primary key (id)
);