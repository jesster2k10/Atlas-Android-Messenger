package com.layer.messenger.util;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.layer.messenger.App;
import com.layer.sdk.messaging.Conversation;

import java.util.HashMap;
import java.util.List;

public class ConversationTaskLoader extends AsyncTaskLoader<HashMap<String, Integer>> {
    private HashMap<String, Integer> mCachedSettingsData;

    public ConversationTaskLoader(Context context) {
        super(context);
    }

    @Override
    public HashMap<String, Integer> loadInBackground() {
        final List<Conversation> conversations = App.getLayerClient().getConversations();
        int totalMessages = 0;
        int totalUnread = 0;
        HashMap<String, Integer> result = new HashMap<>();
        for (Conversation conversation : conversations) {
            totalMessages += conversation.getTotalMessageCount();
            totalUnread += conversation.getTotalUnreadMessageCount();
        }

        result.put(Constants.SETTINGS_TOTAL_MESSAGE_KEY, totalMessages);
        result.put(Constants.SETTINGS_TOTAL_UNREAD_MESSAGE_KEY, totalUnread);
        result.put(Constants.SETTINGS_TOTAL_CONVERSATION_COUNT, conversations.size());

        return result;
    }

    @Override
    protected void onStartLoading() {
        if (mCachedSettingsData == null) {
            forceLoad();
        } else {
            super.deliverResult(mCachedSettingsData);
        }
    }

    @Override
    public void deliverResult(HashMap<String, Integer> data) {
        mCachedSettingsData = data;
        super.deliverResult(data);
    }
}