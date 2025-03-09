= Listing Subscriptions via the Browser.

1. Log into youtube. Go to https://youtube.com/xx/subscriptions
2. **Download** this page. Do not save the source - it will show the AJAX calls, not the results.
3. Open the ```.html``` file in an editor able to handle large files. (You can discard related directory).
4. Search for 'itResp'....
5. Reverse search for ```<script>```. Delete everything up to this point, including the ```<script>```.
6. (remove start)
7. Forward search for ```</script>```. Delete everything from this point, including the ```</script>```.
8. Remove the final ```);```
9. Save as 'subscriptions-raw.json'


Extract key elements

```bash
$ cat subscriptions-raw.json | \
   jq ".contents.twoColumnBrowseResultsRenderer.tabs[0].tabRenderer.content.sectionListRenderer.contents[0].itemSectionRenderer.contents[0].shelfRenderer.content.expandedShelfContentsRenderer.items[].channelRenderer | \
     { \
        channelId: .channelId, \
        title: .title.simpleText, \
        description: .descriptionSnippet.runs[0].text, \
        videoCount: .videoCountText.simpleText, \
        canonicalBaseUrl: .navigationEndpoint.browseEndpoint.canonicalBaseUrl, \
        subscribed: .subscribeButton.subscribeButtonRenderer.subscribed, \
        enabled: .subscribeButton.subscribeButtonRenderer.enabled, \
        type: .subscribeButton.subscribeButtonRenderer.type, \
        currentStateId: .subscribeButton.subscribeButtonRenderer.notificationPreferenceButton.subscriptionNotificationToggleButtonRenderer.currentStateId, \
        showPreferences: .subscribeButton.subscribeButtonRenderer.showPreferences, \
        thumbnails: .thumbnail.thumbnails, \
        subscribedEntityKey : .subscribeButton.subscribeButtonRenderer.subscribedEntityKey \
     }"

#       shortByline : .shortBylineText.runs[0].text, \
#       longByline: .longBylineText.runs[0].text, \
#       browseId: .navigationEndpoint.browseEndpoint.browseId, \
```