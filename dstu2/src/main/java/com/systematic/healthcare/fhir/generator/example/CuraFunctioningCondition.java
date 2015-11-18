package com.systematic.healthcare.fhir.generator.example;

import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import java.util.List;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.dstu2.composite.BoundCodeableConceptDt;
import ca.uhn.fhir.model.dstu2.valueset.ConditionCategoryCodesEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.dstu2.valueset.ConditionClinicalStatusCodesEnum;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.dstu2.valueset.ConditionVerificationStatusEnum;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.RangeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.dstu2.resource.Condition.Stage;
import ca.uhn.fhir.model.dstu2.resource.Condition.Evidence;
@ResourceDef(name = "Condition", id = "Cura Functioning Condition")
public class CuraFunctioningCondition extends Condition {

    /**
     * TODO: Replace Void.class with correct extension type
     *
     * @deprecated Replace Void.class with correct extension type
     */
    @Extension(definedLocally = false, isModifier = false, url = "http://www.systematic.com/fhir/StructureDefinition/SelfRelianceLevel")
    @Child(name = "selfRelianceLevel", min = 0, max = 1, order = Child.REPLACE_PARENT, summary = false, modifier = false)
    @Description(formalDefinition = "Optional Extensions Element - found in all resources.")
    CodeableConceptDt mySelfRelianceLevel;
    @Child(name = "identifier", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = IdentifierDt.class)
    @Description(shortDefinition = "External Ids for this condition", formalDefinition = "This records identifiers associated with this condition that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).")
    List<IdentifierDt> myIdentifier;
    @Child(name = "encounter", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = ResourceReferenceDt.class)
    @Description(shortDefinition = "Encounter when condition first asserted", formalDefinition = "Encounter during which the condition was first asserted.")
    ResourceReferenceDt myEncounter;
    @Child(name = "asserter", min = 1, max = 1, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = ResourceReferenceDt.class)
    @Description(shortDefinition = "Person who asserts this condition", formalDefinition = "Individual who is making the condition statement.")
    ResourceReferenceDt myAsserter;
    @Child(name = "dateRecorded", min = 1, max = 1, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = DateDt.class)
    @Description(shortDefinition = "When first entered", formalDefinition = "A date, when  the Condition statement was documented.")
    DateDt myDateRecorded;
    @Child(name = "code", min = 1, max = 1, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = CodeableConceptDt.class)
    @Description(shortDefinition = "Identification of the condition, problem or diagnosis", formalDefinition = "Identification of the condition, problem or diagnosis.")
    CodeableConceptDt myCode;
    @Child(name = "category", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = CodeableConceptDt.class)
    @Description(shortDefinition = "complaint | symptom | finding | diagnosis", formalDefinition = "A category assigned to the condition.")
    BoundCodeableConceptDt<ConditionCategoryCodesEnum> myCategory;
    @Child(name = "clinicalStatus", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = true, modifier = true, type = CodeDt.class)
    @Description(shortDefinition = "active | relapse | remission | resolved", formalDefinition = "The clinical status of the condition.")
    BoundCodeDt<ConditionClinicalStatusCodesEnum> myClinicalStatus;
    @Child(name = "verificationStatus", min = 1, max = 1, order = Child.REPLACE_PARENT, summary = true, modifier = true, type = CodeDt.class)
    @Description(shortDefinition = "provisional | differential | confirmed | refuted | entered-in-error | unknown", formalDefinition = "The verification status to support the clinical status of the condition.")
    BoundCodeDt<ConditionVerificationStatusEnum> myVerificationStatus;
    @Child(name = "severity", min = 0, max = 1, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = CodeableConceptDt.class)
    @Description(shortDefinition = "Subjective severity of condition", formalDefinition = "A subjective assessment of the severity of the condition as evaluated by the clinician.")
    CodeableConceptDt mySeverity;
    @Child(name = "onset", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = {
            DateTimeDt.class, QuantityDt.class, PeriodDt.class, RangeDt.class,
            StringDt.class})
    @Description(shortDefinition = "Estimated or actual date,  date-time, or age", formalDefinition = "Estimated or actual date or date-time  the condition began, in the opinion of the clinician.")
    IDatatype myOnset;
    @Child(name = "abatement", min = 0, max = 1, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = DateTimeDt.class)
    @Description(shortDefinition = "If/when in resolution/remission", formalDefinition = "The date or estimated date that the condition resolved or went into remission. This is called \"abatement\" because of the many overloaded connotations associated with \"remission\" or \"resolution\" - Conditions are never really resolved, but they can abate.")
    IDatatype myAbatement;
    @Child(name = "stage", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = Stage.class)
    @Description(shortDefinition = "Stage/grade, usually assessed formally", formalDefinition = "Clinical stage or grade of a condition. May include formal severity assessments.")
    Stage myStage;
    @Child(name = "evidence", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = Evidence.class)
    @Description(shortDefinition = "Supporting evidence", formalDefinition = "Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed.")
    List<Evidence> myEvidence;
    @Child(name = "bodySite", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = CodeableConceptDt.class)
    @Description(shortDefinition = "Anatomical location, if relevant", formalDefinition = "The anatomical location where this condition manifests itself.")
    List<CodeableConceptDt> myBodySite;
}