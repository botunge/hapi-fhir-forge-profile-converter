package com.systematic.healthcare.fhir.generator.example;

import ca.uhn.fhir.model.dstu2.composite.*;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import java.util.List;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
@ResourceDef(name = "Condition", id = "FunctioningCondition")
public class FunctioningCondition extends Condition {

    @Child(name = "identifier", type = IdentifierDt.class, min = 0, max = 0, order = Child.REPLACE_PARENT, summary = false, modifier = false)
    @Description(shortDefinition = "External Ids for this condition", formalDefinition = "This records identifiers associated with this condition that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).")
    List myIdentifier;
    @Child(name = "evidence", type = {}, min = 0, max = 0, order = Child.REPLACE_PARENT, summary = false, modifier = false)
    @Description(shortDefinition = "Supporting evidence", formalDefinition = "Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed.")
    List myEvidence;
    @Child(name = "bodySite", type = CodeableConceptDt.class, min = 0, max = 0, order = Child.REPLACE_PARENT, summary = false, modifier = false)
    @Description(shortDefinition = "Anatomical location, if relevant", formalDefinition = "The anatomical location where this condition manifests itself.")
    List myBodySite;
}