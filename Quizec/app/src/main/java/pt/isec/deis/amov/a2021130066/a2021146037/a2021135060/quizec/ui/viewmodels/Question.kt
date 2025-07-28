package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels

import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.QuestionsType

data class Question(
    var title: String, //pergunta
    var questionType: QuestionsType,
    var options: ArrayList<*>? = null,
    var serverPhotoUrl: String? = null,
    var uniqueCode: String, //id
    var quemRespondeu: ArrayList<String>? = null,
    var respostas: ArrayList<*>? = null,
    var idQuestionario: String
)