package year2023.day20

import framework.solution

private typealias Input = Module.Broadcast

private sealed class Module(
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

fun main() = solution<Input>(2023, 20) {

    var modules = mutableMapOf<String, Module>()

    fun recursivelyReadInput(lines: List<String>, name: String): Module {
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

    parseInput { lines ->
        modules = mutableMapOf()
        recursivelyReadInput(lines, "broadcaster") as Module.Broadcast
    }

    partOne { input ->
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
                        val flipFlopModule = module as Module.FlipFlop
                        if (valueToEmit) continue
                        flipFlopModule.isOn = !flipFlopModule.isOn
                        flipFlopModule.inputModules.forEach { m ->
                            if (m is Module.Conjunction)
                                m.connectedInputModules[module.name] = flipFlopModule.isOn
                            q.add(Pair(m, flipFlopModule.isOn))
                            if (flipFlopModule.isOn) highPulses++ else lowPulses++
                        }
                    }
                    is Module.Conjunction -> module.inputModules.forEach { m ->
                        if (m.name == "rx") {
                            valueToEmit = !valueToEmit
                        } else {
                            val conjunctionModule = module as Module.Conjunction
                            valueToEmit = !conjunctionModule.connectedInputModules.keys.map {
                                modules[it] as Module.FlipFlop
                            }.all(Module.FlipFlop::isOn)
                            q.add(Pair(m, valueToEmit))
                        }
                        if (valueToEmit) highPulses++ else lowPulses++
                    }
                }
            }
        }
        lowPulses * highPulses
    }
}
