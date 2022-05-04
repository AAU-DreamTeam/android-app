package androidapp.CO2Mad.tools.quiz.QuestionVariant

import androidapp.CO2Mad.tools.quiz.Question.QuestionType
import kotlin.math.roundToInt

class AbsorptionDaysVariant(emission: Double, questionType: QuestionType): QuestionVariant {
    private val absorptionPerDay = getEffectPerUnit(questionType)
    override val roundToNearest = 5
    override val actualValue = (emission / absorptionPerDay).roundToInt()
    override val quizValue = calcQuizValue()
    override var hasBeenAsked = false
    override var result: Boolean? = null
    override val actualValueStr get() = if (!hasBeenAsked) valueToString(-1) else valueToString(actualValue)
    override val quizValueStr = valueToString(quizValue)
    override val quizEffect = quizValue * absorptionPerDay

    private fun valueToString(value: Int): String {
        val unitSingular = "dag"
        val unitPlural = "dage"

        return if(value == -1) "? $unitPlural" else "$value ${if (value == 1) unitSingular else unitPlural}"
    }
}