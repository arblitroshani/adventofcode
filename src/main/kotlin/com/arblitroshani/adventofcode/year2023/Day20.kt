package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle

typealias Input23d20 = Module.Broadcast

sealed class Module(
    open val name: String,
    open var inputModules: List<Module>,
) {
    data class Broadcast(
        override var inputModules: List<Module>,
    ) : Module("broadcaster", inputModules)

    data class FlipFlop(
        override val name: String,
        override var inputModules: List<Module>,
        var isOn: Boolean = false,
    ) : Module(name, inputModules)

    data class Conjunction(
        override val name: String,
        override var inputModules: List<Module>,
        val connectedInputModules: MutableMap<String, Boolean>,
    ) : Module(name, inputModules)
}

class Day20: AocPuzzle<Input23d20>() {

    private val modules = mutableMapOf<String, Module>()

    override fun parseInput(lines: List<String>): Input23d20 =
        recursivelyReadInput(lines, "broadcaster") as Module.Broadcast

    private fun recursivelyReadInput(lines: List<String>, name: String): Module {
        if (modules[name] != null) return modules[name]!!

        val line: String?
        val module: Module
        val inputs: String
        val inputModules = mutableListOf<Module>()

        if (name == "broadcaster") {
            line = lines.first { it.startsWith(name) }
            inputs = line.split(" -> ").last()
            module = Module.Broadcast(mutableListOf())
        } else {
            line = lines.firstOrNull { it.drop(1).startsWith(name) }
            if (line == null) {
                inputs = ""
                module = Module.FlipFlop(name, mutableListOf())
            } else {
                val (signedName, lineInputs) = line.split(" -> ")
                inputs = lineInputs
                module = if (signedName.first() == '%') {
                    Module.FlipFlop(name, mutableListOf())
                } else {
                    Module.Conjunction(name, mutableListOf(), mutableMapOf())
                }
            }

        }
        modules[name] = module

        for (n in inputs.split(", ")) {
            val m = recursivelyReadInput(lines, n)
            if (m is Module.Conjunction && module is Module.FlipFlop)
                m.connectedInputModules[module.name] = false
            inputModules.add(m)
        }
        module.inputModules = inputModules
        return module
    }

    override fun partOne(): Long {
        var lowPulses = 0L
        var highPulses = 0L
        val q = mutableListOf<Pair<Module, Boolean>>()

        repeat(1000) {
            q.add(Pair(input, false))

            while (q.isNotEmpty()) {
                var (module, valueToEmit) = q.removeFirst()

                when (module) {
                    is Module.Broadcast -> {
                        lowPulses++ // account for the button press
                        module.inputModules.forEach {
                            q.add(Pair(it, false))
                            lowPulses++
                        }
                    }
                    is Module.FlipFlop -> {
                        if (valueToEmit) continue
                        module.isOn = !module.isOn
                        module.inputModules.forEach { m ->
                            if (m is Module.Conjunction)
                                m.connectedInputModules[module.name] = module.isOn
                            q.add(Pair(m, module.isOn))
                            if (module.isOn) highPulses++ else lowPulses++
                        }
                    }
                    is Module.Conjunction -> module.inputModules.forEach { m ->
                        if (m.name == "rx") {
                            valueToEmit = !valueToEmit
                        } else {
                            valueToEmit = !module.connectedInputModules.keys.map {
                                modules[it] as Module.FlipFlop
                            }.all { it.isOn }
                            q.add(Pair(m, valueToEmit))
                        }
                        if (valueToEmit) highPulses++ else lowPulses++
                    }
                }
            }
        }
        return lowPulses * highPulses
    }

    override fun partTwo(): Long {
        // TODO
        return 0
    }
}

fun main() = Day20().solve()
