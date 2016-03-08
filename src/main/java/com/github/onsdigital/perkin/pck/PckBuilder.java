package com.github.onsdigital.perkin.pck;

import com.github.onsdigital.perkin.json.Survey;
import com.github.onsdigital.perkin.pck.derivator.Derivator;
import com.github.onsdigital.perkin.pck.derivator.DerivatorFactory;
import com.github.onsdigital.perkin.pck.derivator.DerivatorNotFoundException;
import com.github.onsdigital.perkin.pck.questions.PCKQuestion;
import com.github.onsdigital.perkin.pck.questions.PCKQuestionTemplate;
import com.github.onsdigital.perkin.pck.survey.SurveyTemplate;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class converts the survey questionnaire data from key/answer pairs into PCK format String.
 * 
 * @author altafm
 * 
 */
public class PckBuilder {

	private static final String HEADER_SEPARATOR = ":";
	private static final String FORM_LEAD = "FV";
	private static final int QUESTION_PAD_LENGTH = 4;

	private DerivatorFactory derivatorFactory = new DerivatorFactory();
    private SurveyTemplate template;

	public PckBuilder() {
        //we only have the MCI survey template for now
        template = getMciSurveyTemplate();
	}

	public Pck build(Survey survey, long batch) throws DerivatorNotFoundException {

        System.out.println("pck building from survey: " + survey);
		Pck pck = new Pck();
		pck.setHeader(generateHeader(batch));
		pck.setFormIdentifier(generateFormIdentifier());
		pck.setQuestions(generatePCKQuestions(survey, template));
		pck.setFormLead(FORM_LEAD);

        //TODO: made up a filename structure for now
        pck.setFilename(batch + "_" + survey.getRespondentId() + ".pck");

        System.out.println("pck built: " + pck);
		
		return pck;
	}
	
	private List <PCKQuestion> generatePCKQuestions(Survey survey, SurveyTemplate surveytemplate) throws DerivatorNotFoundException {
		
		List <PCKQuestion> pckQuestions = new ArrayList<>();
		
		for (PCKQuestionTemplate questionTemplate: surveytemplate.getPckQuestionTemplates()){
			
			String questionNumber = questionTemplate.getQuestionNumber();
			String answer = survey.getAnswer(questionNumber);
			if (!answer.isEmpty()){
				pckQuestions.add(new PCKQuestion(questionNumber, deriveAnswer(questionTemplate, answer)));
			}
		}
		
		return pckQuestions;
	}
	
	private String deriveAnswer(PCKQuestionTemplate question, String surveyAnswer) throws DerivatorNotFoundException {
		Derivator derivator = derivatorFactory.getDerivator(question.getDerivator());
		return derivator.deriveValue(surveyAnswer);
	}
		
	private String generateHeader(long batchId) {

        //TODO: think the date should be a date from the survey
	
		return "FBFV" + String.valueOf(batchId) + getCurrentDateAsString();
	}

	private String generateFormIdentifier() {
		StringBuilder formIdentifer = new StringBuilder();

        //TODO: looks like this is hardcoded for now?
		formIdentifer.append(StringUtils.leftPad("5", QUESTION_PAD_LENGTH, "0"));
		formIdentifer.append(HEADER_SEPARATOR);
		formIdentifer.append("99999994188");
		formIdentifer.append("F");
		formIdentifer.append(HEADER_SEPARATOR);
		String refpStartDate ="01 Oct 2014";
		formIdentifer.append(getFormattedPeriod(refpStartDate));

		return formIdentifer.toString();
	}

	/**
	 * Converts the refp start date String (dd MMM yyyy) into period in 'yyyyMM' format
	 * 
	 * @param refpStartDateStr
	 *            the date String
	 * 
	 * @return String formatted period i.e. yyyyMM
	 * 
	 */
	private String getFormattedPeriod(String refpStartDateStr) {
		DateFormat inputFormatter = new SimpleDateFormat("dd MMMM yyyy");
		DateFormat outputFormatter = new SimpleDateFormat("yyyyMM");
		String period = null;
		try {
			Date refpStartDate = inputFormatter.parse(refpStartDateStr);
			period = outputFormatter.format(refpStartDate);
		} catch (ParseException pe) {
            //TODO: might need to throw exception here? otherwise this will be a silent failure - add a test
			System.out.println("Date parser error :"+ pe.getMessage());
		}
		
		return period;
	}
	
	public static String getCurrentDateAsString(){
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
		String text = date.format(formatter);
		return text;

	}

    private SurveyTemplate getMciSurveyTemplate() {
        return SurveyTemplate.builder()
                .id("023")
                .name("MCI")
                .question(new PCKQuestionTemplate("1", "boolean", true))
                .question(new PCKQuestionTemplate("11", "boolean", false))
                .question(new PCKQuestionTemplate("20", "boolean", false))
                .question(new PCKQuestionTemplate("30", "boolean", true))
                .question(new PCKQuestionTemplate("40", "default", false))
                .question(new PCKQuestionTemplate("50", "default", true))
                .question(new PCKQuestionTemplate("70", "boolean", false))
                .question(new PCKQuestionTemplate("90", "boolean", true))
                .question(new PCKQuestionTemplate("100", "contains", false))
                .build();
    }
}