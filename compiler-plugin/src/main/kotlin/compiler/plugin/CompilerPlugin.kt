package compiler.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.jvm.ir.erasedUpperBound
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.isSingleFieldValueClass
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.getInlineClassUnderlyingType
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

@OptIn(ExperimentalCompilerApi::class)
class CliProcessor : CommandLineProcessor {
    override val pluginId: String = "test.kotlin.plugin"
    override val pluginOptions: Collection<AbstractCliOption> = emptyList()
}

@OptIn(ExperimentalCompilerApi::class)
class CompilerPlugin : CompilerPluginRegistrar() {

    override val supportsK2: Boolean get() = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        IrGenerationExtension.registerExtension(IrExtension())
    }
}

class IrExtension : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        moduleFragment.transformChildrenVoid(
            object : IrElementTransformerVoid() {
                override fun visitValueParameter(declaration: IrValueParameter): IrStatement {
                    if (declaration.type.erasedUpperBound.isSingleFieldValueClass) {
                        val type = declaration.type.classOrNull?.let {
                            getInlineClassUnderlyingType(it.owner).classOrNull?.defaultType
                        }
                        // use type just in case
                        println(type)
                    }
                    return declaration
                }
            }
        )
    }
}