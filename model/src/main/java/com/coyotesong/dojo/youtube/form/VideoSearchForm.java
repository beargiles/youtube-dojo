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

package com.coyotesong.dojo.youtube.form;

/**
 * Video search form.
 *
 * @see "https://developers.google.com/youtube/v3/docs/search/list"
 */
public class VideoSearchForm extends YouTubeSearchForm {
    private String videoCaption;  // any, none, closedCaption  - requires 'type' of video
    private String videoCategoryId;
    private String videoDefinition;  // any, high, standard   (high = 720+)
    private String videoDimension;  // 2d, 3d, any
    private String videoDuration; // any, short, medium, long (any is default)  short < 4 min, 4 <= med <= 20 min, 20 min < long
    private Boolean videoEmbeddable; // any, true
    private String videoLicense; // any, youtube, creativeCommon
    // private String videoPaidProductPlacement; // any, true
    private String videoSyndicated; // any, true
    private String videoType = "any"; // any, episode, movie

    public VideoSearchForm() {
        super("video");
    }

    public String getVideoCaption() {
        return videoCaption;
    }

    public void setVideoCaption(String videoCaption) {
        this.videoCaption = videoCaption;
    }

    public String getVideoCategoryId() {
        return videoCategoryId;
    }

    public void setVideoCategoryId(String videoCategoryId) {
        this.videoCategoryId = videoCategoryId;
    }

    public String getVideoDefinition() {
        return videoDefinition;
    }

    public void setVideoDefinition(String videoDefinition) {
        this.videoDefinition = videoDefinition;
    }

    public String getVideoDimension() {
        return videoDimension;
    }

    public void setVideoDimension(String videoDimension) {
        this.videoDimension = videoDimension;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public Boolean getVideoEmbeddable() {
        return videoEmbeddable;
    }

    public void setVideoEmbeddable(Boolean videoEmbeddable) {
        this.videoEmbeddable = videoEmbeddable;
    }

    public String getVideoLicense() {
        return videoLicense;
    }

    public void setVideoLicense(String videoLicense) {
        this.videoLicense = videoLicense;
    }

    public String getVideoSyndicated() {
        return videoSyndicated;
    }

    public void setVideoSyndicated(String videoSyndicated) {
        this.videoSyndicated = videoSyndicated;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }
}
