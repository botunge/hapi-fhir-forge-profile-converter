package com.systematic.healthcare.fhir.generator.example;

import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import java.util.List;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu2.composite.BoundCodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.dstu2.composite.RangeDt;
import ca.uhn.fhir.model.dstu2.composite.RatioDt;
import ca.uhn.fhir.model.dstu2.composite.SampledDataDt;
import ca.uhn.fhir.model.dstu2.composite.AttachmentDt;
import ca.uhn.fhir.model.primitive.TimeDt;
import ca.uhn.fhir.model.dstu2.resource.Observation.ReferenceRange;
import ca.uhn.fhir.model.dstu2.resource.Observation.Related;
import ca.uhn.fhir.model.dstu2.resource.Observation.Component;
@ResourceDef(name = "Observation", id = "Cura General Assessment Profile")
public class CuraGeneralAssessmentProfile extends Observation {

	@Child(name = "identifier", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = false, modifier = false, type = IdentifierDt.class)
	@Description(shortDefinition = "Unique Id for this particular observation", formalDefinition = "A unique identifier for the simple observation instance.")
	List<IdentifierDt> myIdentifier;
	@Child(name = "category", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = false, modifier = false, type = CodeableConceptDt.class)
	@Description(shortDefinition = "Classification of  type of observation", formalDefinition = "A code that classifies the general type of observation being made.  This is used  for searching, sorting and display purposes.")
	BoundCodeableConceptDt myCategory;
	@Child(name = "code", min = 1, max = 1, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = CodeableConceptDt.class)
	@Description(shortDefinition = "Type of observation (code / type)", formalDefinition = "Describes what was observed. Sometimes this is called the observation \"name\".")
	CodeableConceptDt myCode;
	@Child(name = "subject", min = 1, max = 1, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = ResourceReferenceDt.class)
	@Description(shortDefinition = "The citizen what this is about", formalDefinition = "The citizen whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed. ")
	ResourceReferenceDt mySubject;
	@Child(name = "encounter", min = 0, max = 1, order = Child.REPLACE_PARENT, summary = false, modifier = false, type = ResourceReferenceDt.class)
	@Description(shortDefinition = "Healthcare event during which this observation is made", formalDefinition = "The healthcare event  (e.g. a patient and healthcare provider interaction) during which this observation is made.")
	ResourceReferenceDt myEncounter;
	@Child(name = "effective", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = {
			DateTimeDt.class, PeriodDt.class})
	@Description(shortDefinition = "Clinically relevant time/time-period for observation", formalDefinition = "The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the \"physiologically relevant time\". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself.")
	IDatatype myEffective;
	@Child(name = "performer", min = 1, max = 1, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = ResourceReferenceDt.class)
	@Description(shortDefinition = "Who is responsible for the observation", formalDefinition = "Who was responsible for asserting the observed value as \"true\".")
	List<ResourceReferenceDt> myPerformer;
	@Child(name = "value", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = {
			QuantityDt.class, CodeableConceptDt.class, StringDt.class,
			RangeDt.class, RatioDt.class, SampledDataDt.class,
			AttachmentDt.class, TimeDt.class, DateTimeDt.class, PeriodDt.class})
	@Description(shortDefinition = "Actual result", formalDefinition = "The information determined as a result of making the observation, if the information has a simple value.")
	IDatatype myValue;
	@Child(name = "dataAbsentReason", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = false, modifier = false, type = CodeableConceptDt.class)
	@Description(shortDefinition = "Why the result is missing", formalDefinition = "Provides a reason why the expected value in the element Observation.value[x] is missing.")
	CodeableConceptDt myDataAbsentReason;
	@Child(name = "interpretation", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = false, modifier = false, type = CodeableConceptDt.class)
	@Description(shortDefinition = "High, low, normal, etc.", formalDefinition = "The assessment made based on the result of the observation.  Intended as a simple compact code often placed adjacent to the result value in reports and flow sheets to signal the meaning/normalcy status of the result. Otherwise known as abnormal flag.")
	CodeableConceptDt myInterpretation;
	@Child(name = "comments", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = false, modifier = false, type = StringDt.class)
	@Description(shortDefinition = "Comments about result", formalDefinition = "May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.")
	StringDt myComments;
	@Child(name = "bodySite", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = false, modifier = false, type = CodeableConceptDt.class)
	@Description(shortDefinition = "Observed body part", formalDefinition = "Indicates the site on the subject's body where the observation was made (i.e. the target site).")
	CodeableConceptDt myBodySite;
	@Child(name = "method", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = false, modifier = false, type = CodeableConceptDt.class)
	@Description(shortDefinition = "How it was done", formalDefinition = "Indicates the mechanism used to perform the observation.")
	CodeableConceptDt myMethod;
	@Child(name = "specimen", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = false, modifier = false, type = ResourceReferenceDt.class)
	@Description(shortDefinition = "Specimen used for this observation", formalDefinition = "The specimen that was used when this observation was made.")
	ResourceReferenceDt mySpecimen;
	@Child(name = "device", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = false, modifier = false, type = {
			ResourceReferenceDt.class, ResourceReferenceDt.class})
	@Description(shortDefinition = "(Measurement) Device", formalDefinition = "The device used to generate the observation data.")
	ResourceReferenceDt myDevice;
	@Child(name = "referenceRange", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = false, modifier = false, type = ReferenceRange.class)
	@Description(shortDefinition = "Provides guide for interpretation", formalDefinition = "Guidance on how to interpret the value by comparison to a normal or recommended range.")
	List<ReferenceRange> myReferenceRange;
	@Child(name = "related", min = 0, max = 0, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = Related.class)
	@Description(shortDefinition = "Resource related to this observation", formalDefinition = "A  reference to another resource (usually another Observation but could  also be a QuestionnaireAnswer) whose relationship is defined by the relationship type code.")
	List<Related> myRelated;
	@Child(name = "component", min = 0, max = Child.MAX_UNLIMITED, order = Child.REPLACE_PARENT, summary = true, modifier = false, type = {})
	@Description(shortDefinition = "Component results", formalDefinition = "Some observations have multiple component observations.  These component observations are expressed as separate code value pairs that share the same attributes.  Examples include systolic and diastolic component observations for blood pressure measurement and multiple component observations for genetics observations.")
	List<Component> myComponent;

	public enum ComponentresultsEnum {
		COPING, MOTIVATION, RESOURCES, ROLES, HABITS, OCCUPATIONS, LIFEHISTORY, NETWORK, HEALTHINFORMATION, HEALTHCAREPROFESSIONALCONTACTS, AIDS, HOUSE, MULTISTORYBUILDING, NUMBEROFROOMS, PRIVATEPROPERTY, RENTEDHOUSE, HOUSINGCOORPERATIVE, OWNERORMANAGER, HERITAGEPROPERTY, PERMISSIONFORKEYBOX, ENTRYSUITABILITY, ENTRYCOMMENTS, INTERIORSTAIRCASE, EXTERIORSTAIRCASE, ELEVATOR, EXTERIORLEVELDIFFERENCES, KITCHENSUITABILITY, KITCHENCOMMENTS, GAS, ELECTRIC, LIVINGROOMSUITABILITY, LIVINGROOMCOMMENTS, BEDROOMSUITABILITY, BEDROOMCOMMENT, BATHROOMSUITABILITY, BATHROOMCOMMENT, TOILETPLACEMENT, BATHTUB, BATHTUBWITHSEAT, SHOWER, SINKWITHSHOWERHEAD, DRAIN
	}
}