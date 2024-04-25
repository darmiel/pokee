package com.github.pokee.psql.query;

import com.github.pokee.common.fielder.Fielder;

import java.util.List;

public record NamespaceValues(List<? extends Fielder> values, Fielder dummy) {
}