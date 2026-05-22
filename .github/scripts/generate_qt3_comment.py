#!/usr/bin/env python3
import argparse
import json
import os
from typing import Dict, List, Literal, Optional, Tuple, TypedDict, cast


RegressionStatus = Literal['PASS', 'SKIP', 'FAIL', 'ERROR', 'MISSING']
RegressionReason = Literal[
    'missing-in-candidate',
    'new-in-candidate',
    'status-changed',
    'status-worsened',
    'error-code-changed',
]


class RegressionCase(TypedDict):
    key: str
    className: str
    rawName: str
    name: str
    status: RegressionStatus
    errorCode: Optional[str]
    message: Optional[str]


class RegressionChange(TypedDict):
    key: str
    className: str
    rawName: str
    name: str
    baseline: RegressionCase
    candidate: RegressionCase
    reasons: List[RegressionReason]
    isRegression: bool


class RegressionSummary(TypedDict):
    changedCount: int
    regressionCount: int
    statusChanged: int
    statusWorsened: int
    errorCodeChanged: int
    missingInCandidate: int
    newInCandidate: int


class RegressionReport(TypedDict):
    summary: RegressionSummary
    regressions: List[RegressionChange]
    changes: List[RegressionChange]


def load_count_rows(dir_path: str) -> List[Dict]:
    count_path = os.path.join(dir_path, 'count.json')
    with open(count_path, 'r', encoding='utf-8') as f:
        data = json.load(f)
    if not isinstance(data, list):
        raise ValueError(f"Unexpected format in {count_path}")
    return data


def load_regression_summary(dir_path: Optional[str]) -> Optional[RegressionSummary]:
    if not dir_path:
        return None
    regression_path = os.path.join(dir_path, 'regressions.json')
    if not os.path.exists(regression_path):
        return None
    with open(regression_path, 'r', encoding='utf-8') as f:
        data = json.load(f)
    report = cast(RegressionReport, data)
    return report['summary']


def process_rows(rows: List[Dict]) -> Tuple[List[Tuple[str, int, int, int, int]], Tuple[int, int, int, int]]:
    table_rows: List[Tuple[str, int, int, int, int]] = []
    total_pass = total_fail = total_error = total_skip = 0
    
    for r in rows:
        name = str(r.get('name', 'Unknown'))
        # Extract just the test class name (e.g., "iq.XQueryFn1Test" -> "Fn1Test" or "iq.Fn1Test" -> "Fn1Test")
        test_name = name.split('.')[-1]
        # Remove "XQuery" prefix if present
        if test_name.startswith('XQuery'):
            test_name = test_name[6:]  # Remove "XQuery" (6 characters)
        p = int(r.get('pass', 0))
        f = int(r.get('fail', 0))
        e = int(r.get('error', 0))
        s = int(r.get('skip', 0))
        
        table_rows.append((test_name, p, f, e, s))
        total_pass += p
        total_fail += f
        total_error += e
        total_skip += s

    totals = (total_pass, total_fail, total_error, total_skip)
    return table_rows, totals


def render_table(rows: List[Tuple[str, int, int, int, int]], totals: Tuple[int, int, int, int]) -> str:
    lines = []
    lines.append('| Test Suite | Passing   | Failing | Errors   | Skipped | Total |')
    lines.append('| ---------- | --------- | ------- | -------- | ------- | ----- |')
    for name, p, f, e, s in rows:
        total = p + f + e + s
        lines.append(f"| {name} | {p} | {f} | {e} | {s} | {total} |")
    tp, tf, te, ts = totals
    total_tests = tp + tf + te + ts
    lines.append(f"| **Total**  | **{tp}** | **{tf}**  | **{te}** | **{ts}** | **{total_tests}** |")
    return '\n'.join(lines)


def render_regression_summary(summary: Optional[RegressionSummary]) -> Optional[str]:
    if summary is None:
        return None
    changed_count = summary['changedCount']
    regression_count = summary['regressionCount']
    status_worsened = summary['statusWorsened']
    error_code_changed = summary['errorCodeChanged']
    missing_in_candidate = summary['missingInCandidate']
    new_in_candidate = summary['newInCandidate']
    return (
        f"Regression summary: {regression_count} regressions out of {changed_count} changed tests "
        f"({status_worsened} worsened, {error_code_changed} error-code changes, "
        f"{missing_in_candidate} missing, {new_in_candidate} new)."
    )


def build_comment(
    jsoniq_dir: str,
    xquery_dir: str,
    run_id: str,
    repo_owner: str,
    repo_name: str,
    jsoniq_regressions_dir: Optional[str],
    xquery_regressions_dir: Optional[str]
) -> str:
    jsoniq_rows = load_count_rows(jsoniq_dir)
    xquery_rows = load_count_rows(xquery_dir)
    jsoniq_regressions = load_regression_summary(jsoniq_regressions_dir)
    xquery_regressions = load_regression_summary(xquery_regressions_dir)

    jsoniq_table_rows, jsoniq_totals = process_rows(jsoniq_rows)
    xquery_table_rows, xquery_totals = process_rows(xquery_rows)

    jsoniq_table_md = render_table(jsoniq_table_rows, jsoniq_totals)
    xquery_table_md = render_table(xquery_table_rows, xquery_totals)
    jsoniq_regressions_md = render_regression_summary(jsoniq_regressions)
    xquery_regressions_md = render_regression_summary(xquery_regressions)

    parts = []
    parts.append('## Test Results (qt3tests)')
    parts.append('<details>')
    parts.append('<summary>RumbleDB, XQuery parser</summary>')
    parts.append('')
    if xquery_regressions_md:
        parts.append(xquery_regressions_md)
        parts.append('')
    parts.append(xquery_table_md)
    parts.append('')
    parts.append('</details>')
    parts.append('')
    parts.append('<details>')
    parts.append('<summary>RumbleDB, JSONiq parser</summary>')
    parts.append('')
    if jsoniq_regressions_md:
        parts.append(jsoniq_regressions_md)
        parts.append('')
    parts.append(jsoniq_table_md)
    parts.append('')
    parts.append('</details>')
    parts.append('')
    parts.append(f'[Download detailed test results](https://github.com/{repo_owner}/{repo_name}/actions/runs/{run_id}#artifacts)')
    return '\n'.join(parts)


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument('--jsoniq', required=True, help='Path to JSONiq artifact directory')
    parser.add_argument('--xquery', required=True, help='Path to XQuery artifact directory')
    parser.add_argument('--jsoniq-regressions', help='Path to JSONiq regression artifact directory')
    parser.add_argument('--xquery-regressions', help='Path to XQuery regression artifact directory')
    parser.add_argument('--run-id', required=True, help='GitHub Actions run ID')
    parser.add_argument('--repo-owner', required=True, help='Repository owner')
    parser.add_argument('--repo-name', required=True, help='Repository name')
    args = parser.parse_args()

    body = build_comment(
        args.jsoniq,
        args.xquery,
        args.run_id,
        args.repo_owner,
        args.repo_name,
        args.jsoniq_regressions,
        args.xquery_regressions
    )
    print(body)


if __name__ == '__main__':
    main()
