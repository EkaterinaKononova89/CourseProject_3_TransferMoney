package ru.netology.CourseProject_3_TransferMoney.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import ru.netology.CourseProject_3_TransferMoney.model.Amount;
import ru.netology.CourseProject_3_TransferMoney.model.DataForm;

public class DataFormParamHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    // Определяем, поддерживается ли тип параметра для преобразования
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(DataFormParam.class); //Возвращает, объявлен ли параметр с заданным типом аннотации.
    }

    // Если поддерживается, выполнить соответствующее преобразование
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String cardFromNumber = webRequest.getParameter("cardFromNumber");
        String cardFromValidTill = webRequest.getParameter("cardFromValidTill");
        String cardFromCVV = webRequest.getParameter("cardFromCVV");
        String cardToNumber = webRequest.getParameter("cardToNumber");
        String value = webRequest.getParameter("value"); // not null, валидация на фронте
        String currency = webRequest.getParameter("currency");

        return new DataForm(cardFromNumber, cardFromValidTill, cardFromCVV, cardToNumber, new Amount(Integer.valueOf(value), currency));
    }
}