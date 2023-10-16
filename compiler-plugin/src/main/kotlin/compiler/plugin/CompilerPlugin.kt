package compiler.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.addMember
import org.jetbrains.kotlin.ir.declarations.impl.IrFieldImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrFieldPublicSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrFieldSymbolImpl
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.fields
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import org.jetbrains.kotlin.metadata.ProtoBuf
import org.jetbrains.kotlin.metadata.deserialization.Flags.HAS_ANNOTATIONS
import org.jetbrains.kotlin.metadata.serialization.MutableVersionRequirementTable
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.serialization.DescriptorSerializer
import org.jetbrains.kotlin.serialization.DescriptorSerializerPlugin
import org.jetbrains.kotlin.serialization.SerializerExtension

@OptIn(ExperimentalCompilerApi::class)
class CliProcessor : CommandLineProcessor {
    override val pluginId: String = "test.kotlin.plugin"
    override val pluginOptions: Collection<AbstractCliOption> = emptyList()
}

@OptIn(ExperimentalCompilerApi::class)
class CompilerPlugin : CompilerPluginRegistrar() {

    override val supportsK2: Boolean get() = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        IrGenerationExtension.registerExtension(IrExtension(configuration[CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY]!!))
        DescriptorSerializerPlugin.registerExtension(ClassSerializationPlugin())
    }
}

class IrExtension(val messageCollector: MessageCollector) : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val annotationCls = pluginContext.referenceClass(ClassId.fromString("app.test.Inferred"))!!
        moduleFragment.transformChildrenVoid(
            object : IrElementTransformerVoid() {
                override fun visitClass(declaration: IrClass): IrStatement {
                    declaration.annotations += IrConstructorCallImpl(
                        SYNTHETIC_OFFSET,
                        SYNTHETIC_OFFSET,
                        annotationCls.defaultType,
                        annotationCls.constructors.first(),
                        typeArgumentsCount = 0,
                        constructorTypeArgumentsCount = 0,
                        valueArgumentsCount = 0,
                    )
                    messageCollector.report(
                        CompilerMessageSeverity.WARNING,
                        "Adding annotation to ${declaration.name}"
                    )
                    return super.visitClass(declaration)
                }

                override fun visitValueParameter(declaration: IrValueParameter): IrStatement {
                    val cls = declaration.type.classOrNull!!.owner
                    val hasAnnotation = cls.hasAnnotation(annotationCls)
                    messageCollector.report(
                        CompilerMessageSeverity.WARNING,
                        "${declaration.name} with type ${cls.name}, inferred annotation: $hasAnnotation"
                    )
                    return super.visitValueParameter(declaration)
                }
            }
        )
    }
}

class ClassSerializationPlugin : DescriptorSerializerPlugin {
    private val hasAnnotationFlag = HAS_ANNOTATIONS.toFlags(true)
    override fun afterClass(
        descriptor: ClassDescriptor,
        proto: ProtoBuf.Class.Builder,
        versionRequirementTable: MutableVersionRequirementTable,
        childSerializer: DescriptorSerializer,
        extension: SerializerExtension
    ) {
        if (proto.flags and hasAnnotationFlag == 0) {
            proto.flags = proto.flags or hasAnnotationFlag
        }
    }
}