<#assign finderColsList = finder.getColumns()>

<#--
Basic Cases Table:

+---------------------------+-------------------------------+-------------------------------+
|							|	finder.isCollection()		|	!finder.isCollection()		|
+---------------------------+-------------------------------+-------------------------------+
|	finder.isUnique()		|			Case 1				|			Case 2				|
+---------------------------+-------------------------------+-------------------------------+
|	!finder.isUnique()		|			Case 3				|			Case 4				|
+---------------------------+-------------------------------+-------------------------------+

Combination Cases Table 1:

+---------------------------+-------------------------------+-------------------------------+
|							|	finder.isCollection()		|	!finder.isCollection()		|
+---------------------------+---------------------------------------------------------------+
|	finder.isUnique()		|							Case 5								|
+---------------------------+---------------------------------------------------------------+
|	!finder.isUnique()		|							Case 6								|
+---------------------------+---------------------------------------------------------------+

Combination Cases Table 2:

+---------------------------+-------------------------------+-------------------------------+
|							|	finder.isCollection()		|	!finder.isCollection()		|
+---------------------------+-------------------------------+-------------------------------+
|	finder.isUnique()		|								|								|
+---------------------------|			Case 7				|			Case 8				|
|	!finder.isUnique()		|								|								|
+---------------------------+-------------------------------+-------------------------------+

Combination Cases Table 3:

+---------------------------+-------------------------------+-------------------------------+
|							|	finder.isCollection()		|	!finder.isCollection()		|
+---------------------------+-------------------------------+-------------------------------+
|	finder.isUnique()		|																|
+---------------------------|--------------------------------			Case 9				|
|	!finder.isUnique()		|								|								|
+---------------------------+-------------------------------+-------------------------------+

There are a total of 9 cases. The first 4 cases are the basic cases as show in
the first table.

The additional combination case tables allow us to group the basic cases. For
example:

A combination of case 1 and case 2 is grouped as case 5.

A combination of case 3 and case 4 is grouped as case 6.

A combination of case 1 and case 3 is grouped as case 7.

A combination of case 2 and case 4 is grouped as case 8.

A combination of case 1, case 2, and case 4 is grouped as case 9.

Grouping the basic cases allows us to write the finder implementation with as
little duplicate code as possible.

finder.isUnique() means a literal unique finder because it generates a unique
index at the database level. !finder.isCollection() means a conceptual unique
that may or may not be enforced with a unique index at the database level. Case
9 can be considered a union of the literal and conceptual unique finders.
-->

<#-- Case 1: finder.isCollection() && finder.isUnique() -->

<#if finder.isCollection() && finder.isUnique()>
</#if>

<#-- Case 2: !finder.isCollection() && finder.isUnique() -->

<#if !finder.isCollection() && finder.isUnique()>
</#if>

<#-- Case 3: finder.isCollection() && !finder.isUnique() -->

<#if finder.isCollection() && !finder.isUnique()>
	@Test
	public void testFindBy${finder.name}() throws Exception {
		${entity.name} ${entity.varName} = add${entity.name}();

		<#list finderColsList as finderCol>
			${finderCol.type} ${finderCol.name} =

			<#if finderCol.name == "entryCount">
				1
			<#else>
				${entity.varName}.get${finderCol.methodName}()
			</#if>

			;
		</#list>

		List<${entity.name}> ${entity.varNames} = _persistence.findBy${finder.name}(
			<#list finderColsList as finderCol>
				${finderCol.name}

				<#if finderCol_has_next>
					,
				</#if>
			</#list>
		);

		Assert.assertEquals(1, ${entity.varNames}.size());

		Assert.assertEquals(${entity.varName}.getPrimaryKey(), ${entity.varNames}.get(0).getPrimaryKey());
	}

	@Test
	public void testFindBy${finder.name}NotFound() throws Exception {
		add${entity.name}();

		<#list finderColsList as finderCol>
			${finderCol.type} ${finderCol.name} =

			<#if finderCol.type == "boolean">
				ServiceTestUtil.randomBoolean()
			<#elseif finderCol.type == "double">
				ServiceTestUtil.nextDouble()
			<#elseif finderCol.type == "int">
				ServiceTestUtil.nextInt()
			<#elseif finderCol.type == "long">
				ServiceTestUtil.nextLong()
			<#elseif finderCol.type == "Date">
				ServiceTestUtil.nextDate()
			<#elseif finderCol.type == "String">
				ServiceTestUtil.randomString()
			</#if>

			;
		</#list>

		List<${entity.name}> ${entity.varNames} = _persistence.findBy${finder.name}(
			<#list finderColsList as finderCol>
				${finderCol.name}

				<#if finderCol_has_next>
					,
				</#if>
			</#list>
		);

		Assert.assertEquals(0, ${entity.varNames}.size());
	}

	@Test
	public void testFindBy${finder.name}StartEnd() throws Exception {
		testFindBy${finder.name}StartEnd(0, 5, 1);
	}

	@Test
	public void testFindBy${finder.name}StartEndWrongRange() throws Exception {
		testFindBy${finder.name}StartEnd(5, 0, 0);
	}

	@Test
	public void testFindBy${finder.name}StartEndZeroZero() throws Exception {
		testFindBy${finder.name}StartEnd(0, 0, 0);
	}

	protected void testFindBy${finder.name}StartEnd(int start, int end, int expected) throws Exception {
		${entity.name} ${entity.varName} = add${entity.name}();

		<#list finderColsList as finderCol>
			${finderCol.type} ${finderCol.name} =

			<#if finderCol.name == "entryCount">
				1
			<#else>
				${entity.varName}.get${finderCol.methodName}()
			</#if>

			;
		</#list>

		List<${entity.name}> ${entity.varNames} = _persistence.findBy${finder.name}(
			<#list finderColsList as finderCol>
				${finderCol.name},
			</#list>
			start, end);

		Assert.assertEquals(expected, ${entity.varNames}.size());
	}
</#if>