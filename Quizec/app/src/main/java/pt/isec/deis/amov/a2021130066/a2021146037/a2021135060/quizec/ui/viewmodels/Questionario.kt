package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels

data class Questionario(
    var id: String? = null,
    var active: Boolean? = null,
    var criadorPor: String? = null,
    var data: String? = null,
    var imageUrl: String? = null,
    var nPerguntas: Int? = null,
    var titulo: String? = null,
    var respostas: ArrayList<String>? = null,
    var location: String ?= null
)