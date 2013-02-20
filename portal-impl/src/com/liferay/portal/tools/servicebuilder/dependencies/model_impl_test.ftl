package ${packagePath}.model.impl;

import ${packagePath}.model.${entity.name};

<#if !stringUtil.startsWith(packagePath, "com.liferay.portal") >
	import com.liferay.portal.model.impl.BaseModelImplTestCase;
</#if>

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The base model implementation for the ${entity.name} tests.
 *
 * @author ${author}
 * @see ${entity.name}Impl
 * @see ${packagePath}.model.${entity.name}
 * @see ${packagePath}.model.${entity.name}Model
 * @see ${entity.name}ModelImpl
 * @generated
 */
public class ${entity.name}ModelImplTest extends BaseModelImplTestCase {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a ${entity.humanName} model instance should use the {@link ${packagePath}.model.${entity.name}} interface instead.
	 */

	@BeforeClass
	public static void setUp() {
		BaseModelImplTestCase.setUp();

		_${entity.varName} = new ${entity.name}Impl();

		<#list entity.regularColList as column>
			<#assign autoEscape = true>

			<#assign modelName = packagePath + ".model." + entity.name>

			<#if modelHintsUtil.getHints(modelName, column.name)??>
				<#assign hints = modelHintsUtil.getHints(modelName, column.name)>

				<#if hints["auto-escape"]??>
					<#assign autoEscapeHintValue = hints["auto-escape"]>

					<#if autoEscapeHintValue == "false">
						<#assign autoEscape = false>
					</#if>
				</#if>
			</#if>

			<#if autoEscape && (column.type == "String") && (column.localized == false) >
				_${entity.varName}.set${column.methodName}(UNESCAPED_TEXT);
			</#if>
		</#list>

		_escaped${entity.name} = _${entity.varName}.toEscapedModel();
	}

	public  ${entity.name} getModel() {
		return _${entity.varName};
	}

	public  ${entity.name} getUnescapedModel() {
		return _escaped${entity.name}.toUnescapedModel();
	}

	<#list entity.regularColList as column>
		<#assign autoEscape = true>

		<#assign modelName = packagePath + ".model." + entity.name>

		<#if modelHintsUtil.getHints(modelName, column.name)??>
			<#assign hints = modelHintsUtil.getHints(modelName, column.name)>

			<#if hints["auto-escape"]??>
				<#assign autoEscapeHintValue = hints["auto-escape"]>

				<#if autoEscapeHintValue == "false">
					<#assign autoEscape = false>
				</#if>
			</#if>
		</#if>

		<#if autoEscape && (column.type == "String") && (column.localized == false) >
			<#list autoEscapedAnnotatedMethods as autoEscapedAnnotatedMethod>
				<#assign getterMethod = "get${column.methodName}">

				<#if getterMethod == autoEscapedAnnotatedMethod>
					@Test
					public void testEscaped${column.methodName}() {
						Assert.assertEquals(ESCAPED_TEXT, _escaped${entity.name}.get${column.methodName}());
					}
					<#break>
				</#if>
			</#list>
		</#if>
	</#list>

	private static ${entity.name} _${entity.varName};

	private static ${entity.name} _escaped${entity.name};

}