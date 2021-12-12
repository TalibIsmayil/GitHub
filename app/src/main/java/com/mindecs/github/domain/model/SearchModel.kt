package com.mindecs.github.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class SearchModel(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean? = true,
    @SerializedName("items")
    val items: List<Item>? = null,
    @SerializedName("total_count")
    val totalCount: Int? = null
): Parcelable