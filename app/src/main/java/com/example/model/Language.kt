package com.example.model

enum class AppLanguage(val code: String, val displayName: String, val flag: String) {
    EN("en", "English", "🇬🇧"),
    AR("ar", "العربية", "🇸🇦"),
    FR("fr", "Français", "🇫🇷"),
    ES("es", "Español", "🇪🇸")
}

object StringsHelper {
    private val translations = mapOf(
        "app_title" to mapOf(
            "en" to "BOOM TAAKH!",
            "ar" to "بوم طاخ!",
            "fr" to "BOOM TAAKH!",
            "es" to "¡BOOM TAAKH!"
        ),
        "app_subtitle" to mapOf(
            "en" to "Football Blind Auction & Squad Showdown",
            "ar" to "مزاد كرة القدم الأعمى وتحدي تشكيلة النجوم",
            "fr" to "Enchères Secrètes de Football et Duel d'Équipes",
            "es" to "Subasta Misteriosa de Fútbol y Batalla de Plantillas"
        ),
        "play_single" to mapOf(
            "en" to "Play Solo vs AI",
            "ar" to "لعب فردي ضد الذكاء الاصطناعي",
            "fr" to "Jouer Solo vs IA",
            "es" to "Jugar Solo vs IA"
        ),
        "play_multiplayer" to mapOf(
            "en" to "Online Room & Friends",
            "ar" to "غرف أونلاين ودعوة الأصدقاء",
            "fr" to "Salle En Ligne & Amis",
            "es" to "Sala Online y Amigos"
        ),
        "target_position" to mapOf(
            "en" to "Targeting Position",
            "ar" to "المركز المطلوب بالمزاد",
            "fr" to "Poste Enchère",
            "es" to "Posición en Subasta"
        ),
        "bid_button" to mapOf(
            "en" to "BID",
            "ar" to "زايد الآن",
            "fr" to "ENCHÉRIR",
            "es" to "PUJAR"
        ),
        "pass_button" to mapOf(
            "en" to "PASS",
            "ar" to "انسحب",
            "fr" to "PASSER",
            "es" to "PASAR"
        ),
        "slide_referee" to mapOf(
            "en" to "Slide the Ref 💵",
            "ar" to "رشوة الحكم 💵",
            "fr" to "Glisser au Référent 💵",
            "es" to "Sobornar al Árbitro 💵"
        ),
        "hint_cards" to mapOf(
            "en" to "Hint Cards (15 Wins)",
            "ar" to "كروت التلميحات (١٥ فوز)",
            "fr" to "Cartes Indices (15 Victoires)",
            "es" to "Cartas de Pistas (15 Victorias)"
        ),
        "power_cards" to mapOf(
            "en" to "Power-Up Cards (30 Wins)",
            "ar" to "كروت القوة الإضافية (٣٠ فوز)",
            "fr" to "Cartes Pouvoirs (30 Victoires)",
            "es" to "Cartas de Poder (30 Victorias)"
        ),
        "team_ovr" to mapOf(
            "en" to "Team OVR",
            "ar" to "طاقة الفريق الإجمالية",
            "fr" to "GÉN Équipe",
            "es" to "Media Equipo"
        ),
        "budget" to mapOf(
            "en" to "Auction Budget",
            "ar" to "ميزانية المزاد",
            "fr" to "Budget Enchère",
            "es" to "Presupuesto Subasta"
        ),
        "career_balance" to mapOf(
            "en" to "Club Balance",
            "ar" to "رصيد النادي",
            "fr" to "Solde du Club",
            "es" to "Saldo del Club"
        ),
        "round" to mapOf(
            "en" to "Round",
            "ar" to "الجولة",
            "fr" to "Manche",
            "es" to "Ronda"
        ),
        "referee_says" to mapOf(
            "en" to "REFEREE ANNOUNCEMENT:",
            "ar" to "الحكم يعلن رسمياً:",
            "fr" to "L'ARBITRE ANNONCE:",
            "es" to "EL ÁRBITRO ANUNCIA:"
        ),
        "sold_to" to mapOf(
            "en" to "SOLD TO",
            "ar" to "تم البيع إلى",
            "fr" to "VENDU À",
            "es" to "VENDIDO A"
        ),
        "leaderboard" to mapOf(
            "en" to "Leaderboards",
            "ar" to "لوحة المتصدرين",
            "fr" to "Classement",
            "es" to "Clasificación"
        ),
        "daily_challenges" to mapOf(
            "en" to "Daily Challenges",
            "ar" to "التحديات اليومية",
            "fr" to "Défis Quotidiens",
            "es" to "Desafíos Diarios"
        ),
        "voice_chat" to mapOf(
            "en" to "Voice Chat",
            "ar" to "الدردشة الصوتية",
            "fr" to "Chat Vocal",
            "es" to "Chat de Voz"
        ),
        "final_showdown" to mapOf(
            "en" to "Final Match Showdown",
            "ar" to "المباراة النهائية وحساب القوة",
            "fr" to "Match Final Confrontation",
            "es" to "Duelo Final de Equipos"
        )
    )

    fun get(key: String, lang: AppLanguage): String {
        return translations[key]?.get(lang.code) ?: translations[key]?.get("en") ?: key
    }
}
