package com.sobol.vkcup_e_contentsharing.api

import com.sobol.vkcup_e_contentsharing.models.VKUser
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import java.util.ArrayList

class VKUsersRequest: VKRequest<List<VKUser>> {

    constructor(uids: IntArray = intArrayOf()): super("users.get") {
        if (uids.isNotEmpty()) {
            addParam("user_ids", uids.joinToString(","))
        }
        addParam("fields", "photo_200")
    }

    override fun parse(r: JSONObject): List<VKUser> {
        val users = r.getJSONArray("response")
        val result = ArrayList<VKUser>()
        for (i in 0 until users.length()) {
            result.add(VKUser.parse(users.getJSONObject(i)))
        }
        return result
    }

}