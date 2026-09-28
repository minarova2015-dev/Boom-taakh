package com.example.model

enum class Position(val code: String, val displayName: String, val category: String) {
    GK("GK", "Goalkeeper", "Defense"),
    LB("LB", "Left Back", "Defense"),
    CB("CB", "Center Back", "Defense"),
    RB("RB", "Right Back", "Defense"),
    CDM("CDM", "Defensive Mid", "Midfield"),
    CM("CM", "Central Mid", "Midfield"),
    CAM("CAM", "Attacking Mid", "Midfield"),
    LW("LW", "Left Winger", "Attack"),
    ST("ST", "Striker", "Attack"),
    RW("RW", "Right Winger", "Attack")
}

data class Player(
    val id: String,
    val name: String,
    val position: Position,
    val rating: Int,
    val club: String,
    val league: String,
    val nation: String,
    val flagEmoji: String,
    val age: Int,
    val foot: String,
    val skillStars: Int,
    val pace: Int,
    val shooting: Int,
    val passing: Int,
    val dribbling: Int,
    val defending: Int,
    val physical: Int,
    val baseValueMillions: Int = (rating - 70).coerceAtLeast(5) * 2
)

object PlayerDatabase {
    val allPlayers: List<Player> = listOf(
        // Strikers (ST)
        Player("st_1", "Erling Haaland", Position.ST, 91, "Manchester City", "Premier League", "Norway", "🇳🇴", 24, "Left", 3, 89, 93, 66, 80, 45, 88),
        Player("st_2", "Kylian Mbappé", Position.ST, 91, "Real Madrid", "La Liga", "France", "🇫🇷", 26, "Right", 5, 97, 90, 80, 92, 36, 78),
        Player("st_3", "Harry Kane", Position.ST, 90, "Bayern Munich", "Bundesliga", "England", "🏴󠁧󠁢󠁥󠁮󠁧󠁿", 31, "Right", 3, 65, 93, 84, 83, 49, 83),
        Player("st_4", "Robert Lewandowski", Position.ST, 88, "FC Barcelona", "La Liga", "Poland", "🇵🇱", 36, "Right", 4, 72, 88, 80, 84, 44, 82),
        Player("st_5", "Lautaro Martínez", Position.ST, 89, "Inter Milan", "Serie A", "Argentina", "🇦🇷", 27, "Right", 4, 82, 88, 75, 85, 48, 84),
        Player("st_6", "Victor Osimhen", Position.ST, 87, "Galatasaray", "Super Lig", "Nigeria", "🇳🇬", 26, "Right", 3, 90, 85, 65, 80, 42, 82),
        Player("st_7", "Cristiano Ronaldo", Position.ST, 86, "Al Nassr", "Saudi Pro League", "Portugal", "🇵🇹", 40, "Right", 5, 77, 88, 75, 80, 34, 75),
        Player("st_8", "Karim Benzema", Position.ST, 86, "Al Ittihad", "Saudi Pro League", "France", "🇫🇷", 37, "Right", 4, 75, 86, 81, 84, 38, 76),
        Player("st_9", "Alexander Isak", Position.ST, 85, "Newcastle United", "Premier League", "Sweden", "🇸🇪", 25, "Right", 4, 87, 83, 73, 83, 34, 73),
        Player("st_10", "Julián Álvarez", Position.ST, 84, "Atlético Madrid", "La Liga", "Argentina", "🇦🇷", 25, "Right", 4, 83, 83, 78, 83, 53, 76),

        // Left Wingers (LW)
        Player("lw_1", "Vinícius Júnior", Position.LW, 90, "Real Madrid", "La Liga", "Brazil", "🇧🇷", 24, "Right", 5, 95, 84, 81, 91, 29, 69),
        Player("lw_2", "Son Heung-min", Position.LW, 87, "Tottenham", "Premier League", "South Korea", "🇰🇷", 32, "Both", 4, 87, 88, 80, 84, 42, 69),
        Player("lw_3", "Neymar Jr", Position.LW, 87, "Al Hilal", "Saudi Pro League", "Brazil", "🇧🇷", 33, "Both", 5, 86, 83, 85, 93, 37, 61),
        Player("lw_4", "Rafael Leão", Position.LW, 86, "AC Milan", "Serie A", "Portugal", "🇵🇹", 25, "Right", 4, 93, 81, 76, 87, 27, 78),
        Player("lw_5", "Khvicha Kvaratskhelia", Position.LW, 85, "Napoli", "Serie A", "Georgia", "🇬🇪", 24, "Both", 5, 85, 81, 81, 87, 40, 75),
        Player("lw_6", "Luis Díaz", Position.LW, 84, "Liverpool", "Premier League", "Colombia", "🇨🇴", 28, "Right", 4, 91, 79, 75, 87, 43, 74),
        Player("lw_7", "Gabriel Martinelli", Position.LW, 83, "Arsenal", "Premier League", "Brazil", "🇧🇷", 23, "Right", 4, 89, 78, 76, 85, 45, 72),

        // Right Wingers (RW)
        Player("rw_1", "Lionel Messi", Position.RW, 88, "Inter Miami", "MLS", "Argentina", "🇦🇷", 37, "Left", 4, 79, 87, 90, 92, 33, 64),
        Player("rw_2", "Mohamed Salah", Position.RW, 89, "Liverpool", "Premier League", "Egypt", "🇪🇬", 32, "Left", 4, 89, 87, 82, 88, 45, 76),
        Player("rw_3", "Bukayo Saka", Position.RW, 87, "Arsenal", "Premier League", "England", "🏴󠁧󠁢󠁥󠁮󠁧󠁿", 23, "Left", 4, 86, 83, 83, 87, 65, 76),
        Player("rw_4", "Rodrygo", Position.RW, 86, "Real Madrid", "La Liga", "Brazil", "🇧🇷", 24, "Right", 4, 89, 82, 80, 87, 32, 63),
        Player("rw_5", "Lamine Yamal", Position.RW, 83, "FC Barcelona", "La Liga", "Spain", "🇪🇸", 17, "Left", 5, 85, 78, 81, 86, 30, 52),
        Player("rw_6", "Raphinha", Position.RW, 84, "FC Barcelona", "La Liga", "Brazil", "🇧🇷", 28, "Left", 4, 89, 80, 79, 84, 52, 73),
        Player("rw_7", "Riyad Mahrez", Position.RW, 83, "Al Ahli", "Saudi Pro League", "Algeria", "🇩🇿", 34, "Left", 5, 78, 80, 81, 87, 36, 58),

        // Attacking Midfielders (CAM)
        Player("cam_1", "Kevin De Bruyne", Position.CAM, 90, "Manchester City", "Premier League", "Belgium", "🇧🇪", 33, "Right", 4, 67, 87, 94, 87, 65, 74),
        Player("cam_2", "Jude Bellingham", Position.CAM, 90, "Real Madrid", "La Liga", "England", "🏴󠁧󠁢󠁥󠁮󠁧󠁿", 21, "Right", 4, 80, 86, 83, 88, 78, 83),
        Player("cam_3", "Martin Ødegaard", Position.CAM, 89, "Arsenal", "Premier League", "Norway", "🇳🇴", 26, "Left", 5, 76, 82, 89, 88, 64, 67),
        Player("cam_4", "Jamal Musiala", Position.CAM, 87, "Bayern Munich", "Bundesliga", "Germany", "🇩🇪", 22, "Right", 5, 84, 81, 82, 90, 65, 64),
        Player("cam_5", "Florian Wirtz", Position.CAM, 88, "Bayer Leverkusen", "Bundesliga", "Germany", "🇩🇪", 21, "Right", 4, 81, 81, 86, 89, 52, 68),
        Player("cam_6", "Bruno Fernandes", Position.CAM, 87, "Manchester United", "Premier League", "Portugal", "🇵🇹", 30, "Right", 4, 71, 85, 90, 83, 68, 77),
        Player("cam_7", "Bernardo Silva", Position.CAM, 88, "Manchester City", "Premier League", "Portugal", "🇵🇹", 30, "Left", 4, 69, 78, 86, 92, 69, 74),

        // Central Midfielders (CM)
        Player("cm_1", "Federico Valverde", Position.CM, 88, "Real Madrid", "La Liga", "Uruguay", "🇺🇾", 26, "Right", 3, 88, 82, 84, 84, 80, 84),
        Player("cm_2", "Luka Modrić", Position.CM, 86, "Real Madrid", "La Liga", "Croatia", "🇭🇷", 39, "Both", 4, 72, 75, 89, 87, 72, 65),
        Player("cm_3", "Frenkie de Jong", Position.CM, 87, "FC Barcelona", "La Liga", "Netherlands", "🇳🇱", 27, "Right", 4, 82, 69, 86, 87, 77, 78),
        Player("cm_4", "Nicolò Barella", Position.CM, 87, "Inter Milan", "Serie A", "Italy", "🇮🇹", 28, "Right", 3, 79, 76, 83, 84, 78, 82),
        Player("cm_5", "Pedri", Position.CM, 86, "FC Barcelona", "La Liga", "Spain", "🇪🇸", 22, "Right", 4, 78, 68, 84, 88, 68, 66),
        Player("cm_6", "Alexis Mac Allister", Position.CM, 86, "Liverpool", "Premier League", "Argentina", "🇦🇷", 26, "Right", 3, 70, 80, 84, 82, 76, 77),
        Player("cm_7", "Eduardo Camavinga", Position.CM, 83, "Real Madrid", "La Liga", "France", "🇫🇷", 22, "Left", 4, 82, 66, 80, 82, 80, 81),

        // Defensive Midfielders (CDM)
        Player("cdm_1", "Rodri", Position.CDM, 91, "Manchester City", "Premier League", "Spain", "🇪🇸", 28, "Right", 3, 66, 80, 86, 82, 87, 85),
        Player("cdm_2", "Declan Rice", Position.CDM, 87, "Arsenal", "Premier League", "England", "🏴󠁧󠁢󠁥󠁮󠁧󠁿", 26, "Right", 3, 76, 68, 81, 79, 86, 85),
        Player("cdm_3", "Aurélien Tchouaméni", Position.CDM, 85, "Real Madrid", "La Liga", "France", "🇫🇷", 25, "Right", 3, 73, 71, 79, 78, 84, 83),
        Player("cdm_4", "Joshua Kimmich", Position.CDM, 86, "Bayern Munich", "Bundesliga", "Germany", "🇩🇪", 30, "Right", 3, 70, 75, 88, 84, 80, 78),
        Player("cdm_5", "Casemiro", Position.CDM, 84, "Manchester United", "Premier League", "Brazil", "🇧🇷", 33, "Right", 2, 63, 73, 75, 72, 85, 87),
        Player("cdm_6", "Granit Xhaka", Position.CDM, 85, "Bayer Leverkusen", "Bundesliga", "Switzerland", "🇨🇭", 32, "Left", 3, 60, 76, 84, 76, 80, 83),

        // Center Backs (CB)
        Player("cb_1", "Virgil van Dijk", Position.CB, 89, "Liverpool", "Premier League", "Netherlands", "🇳🇱", 33, "Right", 2, 78, 60, 71, 72, 89, 86),
        Player("cb_2", "Rúben Dias", Position.CB, 88, "Manchester City", "Premier League", "Portugal", "🇵🇹", 27, "Right", 2, 65, 39, 68, 68, 89, 87),
        Player("cb_3", "William Saliba", Position.CB, 87, "Arsenal", "Premier League", "France", "🇫🇷", 23, "Right", 3, 82, 40, 70, 72, 87, 83),
        Player("cb_4", "Antonio Rüdiger", Position.CB, 88, "Real Madrid", "La Liga", "Germany", "🇩🇪", 32, "Right", 2, 82, 55, 71, 66, 86, 86),
        Player("cb_5", "Marquinhos", Position.CB, 87, "Paris Saint-Germain", "Ligue 1", "Brazil", "🇧🇷", 30, "Right", 3, 78, 56, 75, 74, 89, 80),
        Player("cb_6", "Alessandro Bastoni", Position.CB, 87, "Inter Milan", "Serie A", "Italy", "🇮🇹", 25, "Left", 3, 74, 45, 74, 75, 87, 82),
        Player("cb_7", "Ronald Araújo", Position.CB, 85, "FC Barcelona", "La Liga", "Uruguay", "🇺🇾", 26, "Right", 2, 79, 51, 65, 63, 85, 84),
        Player("cb_8", "Gabriel Magalhães", Position.CB, 86, "Arsenal", "Premier League", "Brazil", "🇧🇷", 27, "Left", 2, 68, 42, 65, 68, 86, 84),

        // Left Backs (LB)
        Player("lb_1", "Alphonso Davies", Position.LB, 84, "Bayern Munich", "Bundesliga", "Canada", "🇨🇦", 24, "Left", 4, 95, 68, 77, 84, 76, 77),
        Player("lb_2", "Theo Hernández", Position.LB, 87, "AC Milan", "Serie A", "France", "🇫🇷", 27, "Left", 3, 95, 74, 76, 84, 80, 88),
        Player("lb_3", "Federico Dimarco", Position.LB, 84, "Inter Milan", "Serie A", "Italy", "🇮🇹", 27, "Left", 3, 78, 76, 84, 82, 75, 73),
        Player("lb_4", "Ferland Mendy", Position.LB, 83, "Real Madrid", "La Liga", "France", "🇫🇷", 29, "Left", 4, 91, 64, 75, 78, 81, 84),
        Player("lb_5", "Andrew Robertson", Position.LB, 85, "Liverpool", "Premier League", "Scotland", "🏴󠁧󠁢󠁳󠁣󠁴󠁿", 31, "Left", 3, 80, 61, 81, 79, 80, 76),
        Player("lb_6", "Alejandro Balde", Position.LB, 81, "FC Barcelona", "La Liga", "Spain", "🇪🇸", 21, "Left", 3, 91, 52, 72, 78, 74, 65),

        // Right Backs (RB)
        Player("rb_1", "Achraf Hakimi", Position.RB, 85, "Paris Saint-Germain", "Ligue 1", "Morocco", "🇲🇦", 26, "Right", 4, 92, 76, 80, 81, 76, 78),
        Player("rb_2", "Trent Alexander-Arnold", Position.RB, 86, "Liverpool", "Premier League", "England", "🏴󠁧󠁢󠁥󠁮󠁧󠁿", 26, "Right", 3, 76, 75, 90, 80, 80, 73),
        Player("rb_3", "Dani Carvajal", Position.RB, 86, "Real Madrid", "La Liga", "Spain", "🇪🇸", 33, "Right", 3, 81, 57, 78, 78, 82, 81),
        Player("rb_4", "Kyle Walker", Position.RB, 84, "Manchester City", "Premier League", "England", "🏴󠁧󠁢󠁥󠁮󠁧󠁿", 34, "Right", 2, 89, 63, 77, 77, 80, 81),
        Player("rb_5", "Jeremie Frimpong", Position.RB, 84, "Bayer Leverkusen", "Bundesliga", "Netherlands", "🇳🇱", 24, "Right", 3, 94, 72, 79, 84, 73, 71),
        Player("rb_6", "Ben White", Position.RB, 83, "Arsenal", "Premier League", "England", "🏴󠁧󠁢󠁥󠁮󠁧󠁿", 27, "Right", 2, 78, 48, 76, 76, 82, 77),

        // Goalkeepers (GK)
        Player("gk_1", "Thibaut Courtois", Position.GK, 89, "Real Madrid", "La Liga", "Belgium", "🇧🇪", 32, "Left", 1, 85, 89, 76, 88, 46, 88),
        Player("gk_2", "Alisson Becker", Position.GK, 89, "Liverpool", "Premier League", "Brazil", "🇧🇷", 32, "Right", 1, 86, 85, 85, 89, 54, 90),
        Player("gk_3", "Ederson", Position.GK, 88, "Manchester City", "Premier League", "Brazil", "🇧🇷", 31, "Left", 1, 86, 82, 91, 86, 64, 86),
        Player("gk_4", "Jan Oblak", Position.GK, 88, "Atlético Madrid", "La Liga", "Slovenia", "🇸🇮", 32, "Right", 1, 85, 90, 78, 86, 43, 87),
        Player("gk_5", "Marc-André ter Stegen", Position.GK, 89, "FC Barcelona", "La Liga", "Germany", "🇩🇪", 32, "Right", 1, 86, 85, 89, 91, 47, 85),
        Player("gk_6", "Gianluigi Donnarumma", Position.GK, 89, "Paris Saint-Germain", "Ligue 1", "Italy", "🇮🇹", 26, "Right", 1, 90, 84, 79, 89, 52, 85),
        Player("gk_7", "Yassine Bounou (Bono)", Position.GK, 84, "Al Hilal", "Saudi Pro League", "Morocco", "🇲🇦", 33, "Left", 1, 84, 83, 76, 86, 40, 83),
        Player("gk_8", "Emiliano Martínez", Position.GK, 87, "Aston Villa", "Premier League", "Argentina", "🇦🇷", 32, "Right", 1, 85, 83, 82, 86, 58, 85)
    )

    fun getPlayersForPosition(position: Position): List<Player> {
        return allPlayers.filter { it.position == position }
    }

    fun getRandomPlayerForPosition(position: Position, excludedIds: Set<String> = emptySet()): Player {
        val candidates = allPlayers.filter { it.position == position && !excludedIds.contains(it.id) }
        return if (candidates.isNotEmpty()) {
            candidates.random()
        } else {
            allPlayers.filter { it.position == position }.random()
        }
    }
}
