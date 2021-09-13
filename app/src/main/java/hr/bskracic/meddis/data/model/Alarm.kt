package hr.bskracic.meddis.data.model

class Alarm(val id: Int, var time: String, var repeatType: Int) {

    companion object {
        const val REPEAT_DAILY = 0
        const val REPEAT_WEEKLY = 1
    }
}