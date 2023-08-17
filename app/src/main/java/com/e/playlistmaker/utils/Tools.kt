

fun createTracksCountString(numberOfTracks: Int): String {
    return when (numberOfTracks) {
        11, 12, 13, 14, 15, 16, 17, 18, 19 -> "$numberOfTracks треков"
        else -> when (numberOfTracks % 10) {
            1 -> "$numberOfTracks трек"
            2, 3, 4 -> "$numberOfTracks трека"
            5, 6, 7, 8, 9, 0 -> "$numberOfTracks треков"
            else -> ""
        }
    }
}

fun createMinutesCountString(numberOfMinutes: Int): String {
    return when (numberOfMinutes) {
        11, 12, 13, 14, 15, 16, 17, 18, 19 -> "$numberOfMinutes минут"
        else -> when (numberOfMinutes % 10) {
            1 -> "$numberOfMinutes минута"
            2, 3, 4 -> "$numberOfMinutes минуты"
            5, 6, 7, 8, 9, 0 -> "$numberOfMinutes минут"
            else -> ""
        }
    }
}