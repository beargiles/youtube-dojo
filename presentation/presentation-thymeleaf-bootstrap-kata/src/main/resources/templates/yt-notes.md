# YouTube Notes

## Main page - with dynamic content with mouseover

### Top-level

```html


```

### Single row

(omitting all '<!--css-build:shady-->' tags)

```html
<ytd-rich-grid-row class="style-scope ytd-rich-grid-renderer">
    <div id="contents" class="style-scope ytd-rich-grid-row">
        <ytd-rich-item-renderer class="style-scope ytd-rich-grid-row" items-per-row="2" is-link-card-full-width="">
            <div id="content" class="style-scope ytd-rich-item-renderer">
                <ytd-rich-grid-media class="style-scope ytd-rich-item-renderer" lockup="true">
                    <!-- ADDED... -->
                </ytd-rich-grid-media>
                <ytd-rich-grid-media class="style-scope ytd-rich-item-renderer" lockup="true">
                    <!-- ADDED... -->
                </ytd-rich-grid-media>
        </ytd-rich-item-renderer>
    </div>
</ytd-rich-grid-row>
```

### Single Item

```html

<ytd-rich-item-renderer class="style-scope ytd-rich-grid-row" items-per-row="2" is-link-card-full-width="">
    <div id="content" class="style-scope ytd-rich-item-renderer">
        <ytd-rich-grid-media class="style-scope ytd-rich-item-renderer" lockup="true">
            <div id="dismissible" class="style-scope ytd-rich-grid-media">
                <div id="thumbnail" class="style-scope ytd-rich-grid-media">
                    <ytd-thumbnail rich-grid-thumbnail="" use-hovered-property="" width="9999"
                                   class="style-scope ytd-rich-grid-media" size="large" loaded="">
                        <!-- ADDED... -->
                    </ytd-thumbnail>
                    <ytd-playlist-thumbnail is-double-stack="" use-hovered-property="" width="9999"
                                            class="style-scope ytd-rich-grid-media" thumbnail-size="large"
                                            enable-web-modern-collections-v2="" size="large" hidden="">
                        <!-- ADDED... -->
                    </ytd-playlist-thumbnail>
                </div>
                <div id="thumbnail-underlay" class="style-scope ytd-rich-grid-media" hidden=""></div>
                <div id="details" class="style-scope ytd-rich-grid-media"><a id="avatar-link"
                                                                             class="yt-simple-endpoint style-scope ytd-rich-grid-media"
                                                                             tabindex="-1" title="Nerd Cookies"
                                                                             href="/@NerdCookies">
                    <yt-img-shadow id="avatar" width="48" class="style-scope ytd-rich-grid-media no-transition"
                                   style="background-color: transparent; --darkreader-inline-bgcolor: transparent;"
                                   data-darkreader-inline-bgcolor="" loaded="">
                        <img id="img" draggable="false" class="style-scope yt-img-shadow" alt=""
                             width="48"
                             src="https://yt3.ggpht.com/HmiycjIi00mVJWCxT6sta3JSoVhxXkJ7f-z_ywueoEOWFNzBcTLjh9lKbiiUXgcf7SL-nGKe=s68-c-k-c0x00ffffff-no-rj">
                    </yt-img-shadow>
                </a>
                    <div id="meta" class="style-scope ytd-rich-grid-media">
                    </div>
                    <div id="menu" class="style-scope ytd-rich-grid-media">
                        <ytd-menu-renderer class="style-scope ytd-rich-grid-media" safe-area="">
                            <!-- ADDED... -->
                    </div>
                    <div id="attached-survey" class="style-scope ytd-rich-grid-media"></div>
                </div>

                <!-- the following appears to be static -->
            </div>
            <div id="dismissed" class="style-scope ytd-rich-grid-media">
                <div id="dismissed-content" class="style-scope ytd-rich-grid-media"></div>
            </div>
            <yt-interaction id="interaction" class="extended style-scope ytd-rich-grid-media">
                <div class="stroke style-scope yt-interaction"></div>
                <div class="fill style-scope yt-interaction"></div>
            </yt-interaction>
        </ytd-rich-grid-media>
    </div>
</ytd-rich-item-renderer
```

### ytd-browser

```html

<ytd-browse class="style-scope ytd-page-manager" rounded-container="" darker-dark-theme="" page-subtype="home"
            role="main" mini-guide-visible="">
    <div id="alert-banner" class="style-scope ytd-browse"></div>
    <div id="masthead-ad" class="style-scope ytd-browse"></div>
    <div id="header" class="style-scope ytd-browse"></div>
    <div id="alerts" class="style-scope ytd-browse"></div>

    <ytd-channel-legal-info-renderer class="style-scope ytd-browse" disable-upgrade=""
                                     hidden=""></ytd-channel-legal-info-renderer>
    <ytd-playlist-sidebar-renderer class="style-scope ytd-browse" disable-upgrade=""
                                   hidden=""></ytd-playlist-sidebar-renderer>
    <ytd-playlist-header-renderer class="style-scope ytd-browse" disable-upgrade=""
                                  hidden=""></ytd-playlist-header-renderer>
    <ytd-settings-sidebar-renderer class="style-scope ytd-browse" disable-upgrade=""
                                   hidden=""></ytd-settings-sidebar-renderer>
    <dom-if class="style-scope ytd-browse">
        <template is="dom-if"></template>
    </dom-if>
    <ytd-two-column-browse-results-renderer class="style-scope ytd-browse grid grid-disabled" page-subtype="home"
                                            disable-grid-state-aware="" style="touch-action: pan-down;">
        <div id="primary" class="style-scope ytd-two-column-browse-results-renderer">
            <ytd-rich-grid-renderer class="style-scope ytd-two-column-browse-results-renderer" is-default-grid=""
                                    style="--ytd-rich-grid-item-max-width: 500px; --ytd-rich-grid-item-min-width: 310px; --ytd-rich-grid-items-per-row: 2; --ytd-rich-grid-posts-per-row: 2; --ytd-rich-grid-slim-items-per-row: 3; --ytd-rich-grid-game-cards-per-row: 3;">
                <div id="header" class="style-scope ytd-rich-grid-renderer">
                    <ytd-feed-filter-chip-bar-renderer class="style-scope ytd-rich-grid-renderer" at-start=""
                                                       is-dark-theme="" fluid-width=""
                                                       component-style="FEED_FILTER_CHIP_BAR_STYLE_TYPE_DEFAULT"
                                                       style="--ytd-rich-grid-chips-bar-width: 935px; --ytd-rich-grid-chips-bar-top: 56px;">
                        <div id="chips-wrapper" class="style-scope ytd-feed-filter-chip-bar-renderer">
                            <div id="chips-content" class="style-scope ytd-feed-filter-chip-bar-renderer">
                                <div id="left-arrow" class="style-scope ytd-feed-filter-chip-bar-renderer">
                                    <div id="left-arrow-button" class="style-scope ytd-feed-filter-chip-bar-renderer">
                                        <ytd-button-renderer class="style-scope ytd-feed-filter-chip-bar-renderer"
                                                             button-renderer="" button-next="">
                                            <yt-button-shape>
                                                <button class="yt-spec-button-shape-next yt-spec-button-shape-next--text yt-spec-button-shape-next--mono yt-spec-button-shape-next--size-m yt-spec-button-shape-next--icon-only-default"
                                                        aria-label="Previous" title="" style="">
                                                    <div class="yt-spec-button-shape-next__icon" aria-hidden="true">
                                                        <yt-icon style="width: 24px; height: 24px;">
                                                            <yt-icon-shape class="style-scope yt-icon">
                                                                <icon-shape class="yt-spec-icon-shape">
                                                                    <div style="width: 100%; height: 100%; fill: currentcolor; --darkreader-inline-fill: currentcolor;"
                                                                         data-darkreader-inline-fill="">
                                                                        <svg xmlns="http://www.w3.org/2000/svg"
                                                                             height="24" viewBox="0 0 24 24" width="24"
                                                                             focusable="false"
                                                                             style="pointer-events: none; display: block; width: 100%; height: 100%;">
                                                                            <path d="M14.96 18.96 8 12l6.96-6.96.71.71L9.41 12l6.25 6.25-.7.71z"></path>
                                                                        </svg>
                                                                    </div>
                                                                </icon-shape>
                                                            </yt-icon-shape>
                                                        </yt-icon>
                                                    </div>
                                                    <yt-touch-feedback-shape style="border-radius: inherit;">
                                                        <div class="yt-spec-touch-feedback-shape yt-spec-touch-feedback-shape--touch-response"
                                                             aria-hidden="true">
                                                            <div class="yt-spec-touch-feedback-shape__stroke"
                                                                 style=""></div>
                                                            <div class="yt-spec-touch-feedback-shape__fill"
                                                                 style=""></div>
                                                        </div>
                                                    </yt-touch-feedback-shape>
                                                </button>
                                            </yt-button-shape>
                                            <tp-yt-paper-tooltip fit-to-visible-bounds="" offset="8" role="tooltip"
                                                                 tabindex="-1">
                                                <div id="tooltip" class="hidden style-scope tp-yt-paper-tooltip"
                                                     style-target="tooltip">
                                                    Previous
                                                </div>
                                            </tp-yt-paper-tooltip>
                                        </ytd-button-renderer>
                                    </div>
                                </div>
                                <div id="filter" class="style-scope ytd-feed-filter-chip-bar-renderer"></div>
                                <div id="scroll-container" class="style-scope ytd-feed-filter-chip-bar-renderer"
                                     style="touch-action: pan-y;">
                                    <iron-selector id="chips" activate-event="" role="tablist"
                                                   selected-attribute="selected"
                                                   class="style-scope ytd-feed-filter-chip-bar-renderer"
                                                   style="transform: translateX(0px);">
                                        <!-- LIST OF yt-chip-cloud-chip-renderer -->
                                    </iron-selector>
                                </div>
                                <div id="right-arrow" class="style-scope ytd-feed-filter-chip-bar-renderer">
                                    <div id="right-arrow-button" class="style-scope ytd-feed-filter-chip-bar-renderer">
                                        <ytd-button-renderer class="style-scope ytd-feed-filter-chip-bar-renderer"
                                                             button-renderer="" button-next="">
                                            <yt-button-shape>
                                                <button class="yt-spec-button-shape-next yt-spec-button-shape-next--text yt-spec-button-shape-next--mono yt-spec-button-shape-next--size-m yt-spec-button-shape-next--icon-only-default"
                                                        aria-label="Next" title="" style="">
                                                    <div class="yt-spec-button-shape-next__icon" aria-hidden="true">
                                                        <yt-icon style="width: 24px; height: 24px;">
                                                            <yt-icon-shape class="style-scope yt-icon">
                                                                <icon-shape class="yt-spec-icon-shape">
                                                                    <div style="width: 100%; height: 100%; fill: currentcolor; --darkreader-inline-fill: currentcolor;"
                                                                         data-darkreader-inline-fill="">
                                                                        <svg xmlns="http://www.w3.org/2000/svg"
                                                                             height="24" viewBox="0 0 24 24" width="24"
                                                                             focusable="false"
                                                                             style="pointer-events: none; display: block; width: 100%; height: 100%;">
                                                                            <path d="m9.4 18.4-.7-.7 5.6-5.6-5.7-5.7.7-.7 6.4 6.4-6.3 6.3z"></path>
                                                                        </svg>
                                                                    </div>
                                                                </icon-shape>
                                                            </yt-icon-shape>
                                                        </yt-icon>
                                                    </div>
                                                    <yt-touch-feedback-shape style="border-radius: inherit;">
                                                        <div class="yt-spec-touch-feedback-shape yt-spec-touch-feedback-shape--touch-response"
                                                             aria-hidden="true">
                                                            <div class="yt-spec-touch-feedback-shape__stroke"
                                                                 style=""></div>
                                                            <div class="yt-spec-touch-feedback-shape__fill"
                                                                 style=""></div>
                                                        </div>
                                                    </yt-touch-feedback-shape>
                                                </button>
                                            </yt-button-shape>
                                            <tp-yt-paper-tooltip fit-to-visible-bounds="" offset="8" role="tooltip"
                                                                 tabindex="-1" style="inset: 52px auto auto 49.6094px;">
                                                <div id="tooltip" class="style-scope tp-yt-paper-tooltip hidden"
                                                     style-target="tooltip">
                                                    Next
                                                </div>
                                            </tp-yt-paper-tooltip>
                                        </ytd-button-renderer>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </ytd-feed-filter-chip-bar-renderer>
                </div>
                <div id="big-yoodle" class="style-scope ytd-rich-grid-renderer"></div>
                <div id="masthead-ad" class="style-scope ytd-rich-grid-renderer"></div>
                <div id="title-container" class="style-scope ytd-rich-grid-renderer" hidden="">
                    <div id="title" class="style-scope ytd-rich-grid-renderer"></div>
                </div>
                <div id="spinner-container" class="style-scope ytd-rich-grid-renderer">
                    <!-- spinner -->
                </div>
                <div id="contents" class="style-scope ytd-rich-grid-renderer">
                    <ytd-rich-grid-row class="style-scope ytd-rich-grid-renderer">
                        <!-- REPEATED... -->
                    </ytd-rich-grid-row>
                    <ytd-continuation-item-renderer class="style-scope ytd-rich-grid-renderer">
                        <div id="ghost-cards" class="style-scope ytd-continuation-item-renderer">
                            <ytd-ghost-grid-renderer class="style-scope ytd-continuation-item-renderer"
                                                     items-per-row="2">
                                <div class="ghost-grid style-scope ytd-ghost-grid-renderer">
                                    <!-- LIST OF 'ghost-card' divs -->
                                    <dom-repeat class="style-scope ytd-ghost-grid-renderer">
                                        <template is="dom-repeat"></template>
                                    </dom-repeat>
                                </div>
                            </ytd-ghost-grid-renderer>
                        </div>
                        <tp-yt-paper-spinner id="spinner" class="style-scope ytd-continuation-item-renderer"
                                             aria-hidden="true">
                            <!-- spinner -->
                        </tp-yt-paper-spinner>
                        <div id="button" class="style-scope ytd-continuation-item-renderer" hidden=""></div>
                    </ytd-continuation-item-renderer>
                </div>
            </ytd-rich-grid-renderer>
        </div>
        <div id="secondary" class="style-scope ytd-two-column-browse-results-renderer">
        </div>
    </ytd-two-column-browse-results-renderer>
    <div id="survey" class="style-scope ytd-browse"></div>
    <div id="metadata" class="style-scope ytd-browse"></div>
    <div id="footer" class="style-scope ytd-browse"></div>
    <ytd-refresh id="refresh" class="style-scope ytd-browse" style="opacity: 0;">
        <!-- icon and spinner... -->
    </ytd-refresh>
</ytd-browse>

```