package com.expense.tracker.core.data.repo

import android.content.Context
import android.provider.Telephony
import com.expense.tracker.core.domain.models.SmsMessage
import com.expense.tracker.core.domain.repo.SmsRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SmsRepoImpl @Inject constructor(@param:ApplicationContext private val context: Context) : SmsRepo {
    override fun getAllSms(): List<SmsMessage> {
        val smsList = mutableListOf<SmsMessage>()
        val lastSync = getLastSync(context)
        val cursor = context.contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null,
            "date > ?",
            arrayOf(lastSync.toString()),
            "${Telephony.Sms.DATE} ASC"
        )
        var latestTime = lastSync

        cursor?.use {

            val addressIdx =
                it.getColumnIndex(Telephony.Sms.ADDRESS)

            val bodyIdx =
                it.getColumnIndex(Telephony.Sms.BODY)

            val dateIdx =
                it.getColumnIndex(Telephony.Sms.DATE)

            while (it.moveToNext()) {

                val address =
                    if (addressIdx != -1) it.getString(addressIdx) else ""

                val body =
                    if (bodyIdx != -1) it.getString(bodyIdx) else ""

                val date =
                    if (dateIdx != -1) it.getLong(dateIdx) else 0L
                val id = it.getLong(it.getColumnIndexOrThrow(Telephony.Sms._ID))

                smsList.add(
                    SmsMessage(
                        id = id,
                        address = address ?: "",
                        body = body ?: "",
                        date = date
                    )
                )

                if (date > latestTime) {
                    latestTime = date
                }
            }
        }
        saveLastSync(context, latestTime)
        return smsList
    }
}

const val KEY_LAST_SYNC = "last_sms_sync_time"

private fun saveLastSync(context: Context, time: Long) {
    val prefs = context.getSharedPreferences("sms_prefs", Context.MODE_PRIVATE)
    prefs.edit().putLong(KEY_LAST_SYNC, time).apply()
}

private fun getLastSync(context: Context): Long {
    val prefs = context.getSharedPreferences("sms_prefs", Context.MODE_PRIVATE)
    return prefs.getLong(KEY_LAST_SYNC, 0L) // first time = 0
}