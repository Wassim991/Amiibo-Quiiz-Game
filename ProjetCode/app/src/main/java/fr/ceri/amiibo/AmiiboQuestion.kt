package fr.ceri.amiibo

data class AmiiboQuestion(
    val imageUrl: String,
    val propositions: List<String>,
    val bonneReponse: String,
    val typeQuestion: String
)
