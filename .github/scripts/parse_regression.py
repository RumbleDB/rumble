#!/usr/bin/env python3
import json
import os
from typing import Dict, List, Literal, Optional, TypedDict, cast

RegressionStatus = Literal["PASS", "SKIP", "FAIL", "ERROR", "MISSING"]
RegressionReason = Literal[
    "missing-in-candidate",
    "new-in-candidate",
    "status-changed",
    "status-worsened",
    "error-code-changed",
]

ParserName = Literal["jsoniq", "xquery"]


class RegressionCaseResult(TypedDict):
    message: Optional[str]
    status: RegressionStatus
    errorCode: Optional[str]


class RegressionCase(TypedDict):
    testCase: str
    candidate: RegressionCaseResult
    baseline: RegressionCaseResult
    reasons: List[RegressionReason]
    transition: str
    suite: str
    id: str
    parser: ParserName


class RegressionBucket(TypedDict):
    cases: List[RegressionCase]
    count: int


class PresenceChanges(TypedDict):
    missingInCandidate: RegressionBucket
    newInCandidate: RegressionBucket


class RegressionCounts(TypedDict):
    presenceChanges: int
    regressions: int
    changed: int
    neutralChanges: int
    improvements: int


class SuiteRegressionReport(TypedDict):
    counts: RegressionCounts
    presenceChanges: PresenceChanges
    regressions: Dict[str, RegressionBucket]
    neutralChanges: Dict[str, RegressionBucket]
    improvements: Dict[str, RegressionBucket]


class ParserRegressionReport(TypedDict):
    counts: RegressionCounts
    suite: Dict[str, SuiteRegressionReport]


class RegressionReport(TypedDict, total=False):
    jsoniq: ParserRegressionReport
    xquery: ParserRegressionReport


def load_regression_report(
    dir_path: Optional[str],
) -> Optional[RegressionReport]:
    if not dir_path:
        return None
    regression_path = os.path.join(dir_path, "regressions.json")
    if not os.path.exists(regression_path):
        return None
    with open(regression_path, "r", encoding="utf-8") as f:
        data = json.load(f)
    report = cast(RegressionReport, data)
    return report


def render_regression_summary(summary: RegressionCounts) -> Optional[str]:
    changed_count = summary["changed"]
    regression_count = summary["regressions"]
    improvement_count = summary["improvements"]
    neutral_change_count = summary["neutralChanges"]
    presence_change_count = summary["presenceChanges"]
    return (
        f"Change summary: {changed_count} changed, {regression_count} regressions, "
        f"{improvement_count} improvements, {neutral_change_count} neutral, "
        f"{presence_change_count} presence changes."
    )


def _render_suite_table(parser_report: ParserRegressionReport) -> Optional[str]:
    rows: List[str] = []
    for suite_name, suite_report in sorted(parser_report["suite"].items()):
        counts = suite_report["counts"]
        rows.append(
            f"| `{suite_name}` | {counts['regressions']} | {counts['improvements']} | "
            f"{counts['neutralChanges']} | {counts['presenceChanges']} | {counts['changed']} |"
        )

    if not rows:
        return None

    lines = [
        "| Suite | Regressions | Improvements | Neutral | Presence | Changed |",
        "| --- | ---: | ---: | ---: | ---: | ---: |",
    ]
    lines.extend(rows)
    return "\n".join(lines)


def render_regression_details(
    dir_path: Optional[str], parser_name: ParserName
) -> Optional[str]:
    regression_report = load_regression_report(dir_path)
    parser_report = regression_report.get(parser_name) if regression_report else None

    if parser_report is None:
        return None

    parts: List[str] = []
    summary = render_regression_summary(parser_report["counts"])
    if summary:
        parts.append(summary)

    suite_table = _render_suite_table(parser_report)
    if suite_table:
        parts.append("")
        parts.append(suite_table)

    return "\n".join(parts)
