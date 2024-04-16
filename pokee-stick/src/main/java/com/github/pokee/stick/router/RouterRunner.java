package com.github.pokee.stick.router;

import java.util.List;
import java.util.Map;

public record RouterRunner(Map<String, String> parameters, List<Handler> handlers) {
}
