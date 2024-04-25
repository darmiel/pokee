package com.github.pokee.psql.query;

import com.github.pokee.common.fielder.Fielder;

import java.util.List;

public record NamespaceValues(List<Fielder> values, Fielder dummy) {
}