package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens

enum class Screens(val display: String) {
    Create("Create"), //Criar/Editar Survey
    Join("Join"), //Responder Survey
    Library("Library"), //Lista de Surveys
    Answer("Answer"), //Lista de respostas survey
    Statistics("Statistics"), //Lista de Surveys que irão mostrar as estatisticas

    SurveyStats("Stats"),

    ThankYou("Thank you"), //After Answering
    HostSurvey("Host"), //Pedir info sobre host survey
    WaitHost("WaitHost"), //Wait for people when hosting

    CreateSurvey("CreateSurvey"), //Adicionar/Eliminar/Editar perguntas ao survey
    DetailsSurvey("SurveyDetails"), //Mostra detalhes do survey

    Survey("Survey"), //Carrega info survey
    SurveyQuestions("SurveyQuestions"), //Carrega perguntas do survey para responder

    Credits("credits");
}