package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle
import com.arblitroshani.adventofcode.util.InputParsing

data class Input23d19(
    val workflows: Map<WorkflowName, Workflow>,
    val parts: List<Part>,
)

data class Workflow(
    val name: WorkflowName,
    val steps: List<Step>,
    val finalStepTarget: StepTarget,
)

typealias WorkflowName = String

data class Step(
    val category: Category,
    val shouldBeBigger: Boolean,
    val targetAmount: Int,
    val next: StepTarget,
)

sealed class StepTarget {
    data object Approve : StepTarget()
    data object Reject : StepTarget()
    data class Redirect(val next: WorkflowName) : StepTarget()

    companion object {
        fun from(s: String): StepTarget = when(s) {
            "A" -> Approve
            "R" -> Reject
            else -> Redirect(s)
        }
    }
}

enum class Category {
    X, M, A, S;

    companion object {
        fun from(s: String): Category = when(s) {
            "x" -> X
            "m" -> M
            "a" -> A
            else -> S
        }
    }
}

data class Part(
    val categoryTargets: Map<Category, Int>,
)

class Day19: AocPuzzle<Input23d19>() {

    override fun parseInput(lines: List<String>): Input23d19 {
        val (workflowLines, partLines) = InputParsing.splitListByEmptyLines(lines)
        return Input23d19(
            workflows = workflowLines.associate { line ->
                val (name, stepsList) = line.split('{', '}')
                val steps = stepsList.split(',')
                val finalStepTarget = StepTarget.from(steps.last())
                val workflowSteps = steps.dropLast(1).map { step ->
                    val (categoryAndTargetAmount, stepTarget) = step.split(':')
                    val next = StepTarget.from(stepTarget)
                    if (categoryAndTargetAmount.contains('<')) {
                        val (category, targetAmount) = categoryAndTargetAmount.split('<')
                        Step(Category.from(category), false, targetAmount.toInt(), next)
                    } else {
                        val (category, targetAmount) = categoryAndTargetAmount.split('>')
                        Step(Category.from(category), true, targetAmount.toInt(), next)
                    }
                }
                name to Workflow(name, workflowSteps, finalStepTarget)
            },
            parts = partLines.map { line ->
                Part(
                    categoryTargets = line.drop(1).dropLast(1)
                        .split(',')
                        .associate { ct ->
                            val (c, targetAmount) = ct.split('=')
                            Category.from(c) to targetAmount.toInt()
                        }
                )
            },
        )
    }

    private val startStep = StepTarget.Redirect("in")

    override fun partOne(): Int =
        input.parts.sumOf { part ->
            var nextStep: StepTarget = startStep
            while (nextStep is StepTarget.Redirect) {
                val workflow = input.workflows[nextStep.next]!!
                var found = false
                for (step in workflow.steps) {
                    if (found) break
                    val partTarget = part.categoryTargets[step.category]!!
                    if ((step.shouldBeBigger && partTarget > step.targetAmount)
                        || (!step.shouldBeBigger && partTarget < step.targetAmount)
                    ) {
                        nextStep = step.next
                        found = true
                    }
                }
                if (!found) nextStep = workflow.finalStepTarget
            }
            if (nextStep is StepTarget.Approve) part.categoryTargets.values.sum() else 0
        }

    override fun partTwo(): Long =
        validRanges(
            st = startStep,
            ranges = Category.entries.associateWith { 1 .. 4000 }.toMutableMap(),
        )

    private fun validRanges(st: StepTarget, ranges: MutableMap<Category, IntRange>): Long =
        when (st) {
            is StepTarget.Reject -> 0
            is StepTarget.Approve -> ranges.values.map(IntRange::count).fold(1, Long::times)
            is StepTarget.Redirect -> {
                val workflow = input.workflows[st.next]!!
                workflow.steps.sumOf { step ->
                    val rangeIfTrue = ranges.toMutableMap()
                    if (step.shouldBeBigger) {
                        rangeIfTrue[step.category] = step.targetAmount + 1 .. ranges[step.category]!!.last
                        ranges[step.category] = ranges[step.category]!!.first .. step.targetAmount
                    } else {
                        rangeIfTrue[step.category] = ranges[step.category]!!.first ..< step.targetAmount
                        ranges[step.category] = step.targetAmount .. ranges[step.category]!!.last
                    }
                    validRanges(step.next, rangeIfTrue)
                } + validRanges(workflow.finalStepTarget, ranges)
            }
        }
}

fun main() = Day19().solve(
    expectedAnswerForSampleInP1 = 19114,
    expectedAnswerForSampleInP2 = 167409079868000L,
)
