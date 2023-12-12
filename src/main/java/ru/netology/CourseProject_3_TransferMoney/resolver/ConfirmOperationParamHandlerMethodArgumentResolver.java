package ru.netology.CourseProject_3_TransferMoney.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import ru.netology.CourseProject_3_TransferMoney.model.ConfirmOperation;

public class ConfirmOperationParamHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    // Определяем, поддерживается ли тип параметра для преобразования
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ConfirmOperationParam.class); //Возвращает, объявлен ли параметр с заданным типом аннотации.
    }

    // Если поддерживается, выполнить соответствующее преобразование
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String operationId = webRequest.getParameter("operationId");
        String code = webRequest.getParameter("code");

        return new ConfirmOperation(operationId, code);
    }
}
