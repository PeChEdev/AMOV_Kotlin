package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens

enum class QuestionsType (val displayName: String) {
    TRUE_FALSE( "1 – Yes/No, True/False"),
    SINGLE_CHOICE("2 – Multiple Choice (Single)"),
    MULTIPLE_CHOICE("3 – Multiple Choice (Multiple)"),
    MATCHING("4 – Columns Association"),
    ORDERING("5 – Ordering"),
    FILL_BLANK("6 – Fill-in-the-Blank"),
    ASSOCIATION("7 – Image/Concept Assoc."),
    WORD_INPUT("8 – Free Response"),
    EMPTY("0")
}