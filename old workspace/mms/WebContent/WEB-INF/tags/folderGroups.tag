<%@ attribute name="list" required="true" type="java.util.ArrayList"%>
<%@ attribute name="item" required="true"%>
<%@ attribute name="view" required="true"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${!empty list}">
   <ul>
   		<c:set var="Lv" value="${item+1}"/>
	    <c:forEach var="folderGroup" items="${list}">
		    		<li id="${folderGroup.folderId}" 
		    			sgid="<c:out value="${folderGroup.sgId}"/>" 
		    			docsetid="<c:out value="${folderGroup.docIdSet}"/>" 
		    			apprvstatus="<c:out value="${folderGroup.approveDoc}"/>"  
		    			<c:choose>
		    				<c:when test="${Lv < 3 && view != 3}">
		    					class="expanded folder"
		    				</c:when>
		    				<c:otherwise> 
		    					class="folder"
		    					</c:otherwise>
		    				</c:choose> >${folderGroup.name}
	        <myTags:folderGroups list="${folderGroup.subGroups}" item="${Lv}" view="${view}"/>
	    </c:forEach>
	</ul>
</c:if>






