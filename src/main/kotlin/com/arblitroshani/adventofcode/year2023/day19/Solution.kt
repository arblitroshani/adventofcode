package com.arblitroshani.adventofcode.year2023.day19

import com.arblitroshani.adventofcode.framework.solution
import com.arblitroshani.adventofcode.util.InputParsing
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

private data class Input(
    val workflows: Map<WorkflowName, Workflow>,
    val parts: List<Part>,
)

private data class Workflow(
    val name: WorkflowName,
    val steps: List<Step>,
    val finalStepTarget: StepTarget,
)

private typealias WorkflowName = String

private data class Step(
    val category: Category,
    val shouldBeBigger: Boolean,
    val targetAmount: Int,
    val next: StepTarget,
)

private sealed class StepTarget {
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

private enum class Category {
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

private data class Part(
    val categoryTargets: Map<Category, Int>,
)

fun main() = solution<Input>(2023, 19) {

    val startStep = StepTarget.Redirect("in")

    fun validRanges(input: Input, st: StepTarget, ranges: MutableMap<Category, IntRange>): Long =
        when (st) {
            is StepTarget.Reject -> 0
            is StepTarget.Approve -> ranges.values.map(IntRange::count).fold(1, Long::times)
            is StepTarget.Redirect -> {
                val workflow = input.workflows[st.next]!!
                workflow.steps.sumOf { step ->
                    val rangeIfTrue = ranges.toMutableMap()
                    if (step.shouldBeBigger) {
                        rangeIfTrue[step.category] =
                            step.targetAmount + 1..ranges[step.category]!!.last
                        ranges[step.category] = ranges[step.category]!!.first..step.targetAmount
                    } else {
                        rangeIfTrue[step.category] =
                            ranges[step.category]!!.first..<step.targetAmount
                        ranges[step.category] = step.targetAmount..ranges[step.category]!!.last
                    }
                    validRanges(input, step.next, rangeIfTrue)
                } + validRanges(input, workflow.finalStepTarget, ranges)
            }
        }

    parseInput { lines ->
        val (workflowLines, partLines) = InputParsing.splitListByEmptyLines(lines)
        Input(
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

    partOne { input ->
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
    }

    partTwo { input ->
        validRanges(
            input = input,
            st = startStep,
            ranges = Category.entries.associateWith { 1..4000 }.toMutableMap(),
        )
    }

    val testInput = """
        px{a<2006:qkq,m>2090:A,rfg}
        pv{a>1716:R,A}
        lnx{m>1548:A,A}
        rfg{s<537:gd,x>2440:R,A}
        qs{s>3448:A,lnx}
        qkq{x<1416:A,crn}
        crn{x>2662:A,R}
        in{s<1351:px,qqz}
        qqz{s>2770:qs,m<1801:hdj,R}
        gd{a>3333:R,R}
        hdj{m>838:A,pv}

        {x=787,m=2655,a=1222,s=2876}
        {x=1679,m=44,a=2067,s=496}
        {x=2036,m=264,a=79,s=2244}
        {x=2461,m=1339,a=466,s=291}
        {x=2127,m=1623,a=2188,s=1013}
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 19114
    }

    partTwoTest {
        testInput shouldOutput 167409079868000
    }
}
