#!/usr/bin/env python3
import argparse
import json
import os
from typing import Dict, List, Optional, Tuple

from parse_regression import render_regression_details


def load_count_rows(dir_path: str) -> List[Dict]:
    count_path = os.path.join(dir_path, 'count.json')
    with open(count_path, 'r', encoding='utf-8') as f:
        data = json.load(f)
    if not isinstance(data, list):
        raise ValueError(f"Unexpected format in {count_path}")
    return data


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


def build_comment(
    jsoniq_dir: str,
    xquery_dir: str,
    run_id: str,
    baseline_run_id: Optional[str],
    repo_owner: str,
    repo_name: str,
    jsoniq_regressions_dir: Optional[str],
    xquery_regressions_dir: Optional[str],
) -> str:
    jsoniq_rows = load_count_rows(jsoniq_dir)
    xquery_rows = load_count_rows(xquery_dir)

    jsoniq_table_rows, jsoniq_totals = process_rows(jsoniq_rows)
    xquery_table_rows, xquery_totals = process_rows(xquery_rows)

    jsoniq_table_md = render_table(jsoniq_table_rows, jsoniq_totals)
    xquery_table_md = render_table(xquery_table_rows, xquery_totals)
    jsoniq_regression_md = render_regression_details(jsoniq_regressions_dir, "jsoniq")
    xquery_regression_md = render_regression_details(xquery_regressions_dir, "xquery")
    artifacts_url = f'https://github.com/{repo_owner}/{repo_name}/actions/runs/{run_id}#artifacts'

    parts = []
    parts.append('## Test Results (qt3tests)')
    if baseline_run_id:
        parts.append(
            f'Regression baseline: [run {baseline_run_id}]'
            f'(https://github.com/{repo_owner}/{repo_name}/actions/runs/{baseline_run_id})'
        )
        parts.append('')
    parts.append('<details>')
    parts.append('<summary>RumbleDB, XQuery parser</summary>')
    parts.append('')
    if xquery_regression_md:
        parts.append(xquery_regression_md)
        parts.append('')
        parts.append(f'Full regression report: see `regressions-xquery` in [artifacts]({artifacts_url}).')
        parts.append('')
    parts.append(xquery_table_md)
    parts.append('')
    parts.append('</details>')
    parts.append('')
    parts.append('<details>')
    parts.append('<summary>RumbleDB, JSONiq parser</summary>')
    parts.append('')
    if jsoniq_regression_md:
        parts.append(jsoniq_regression_md)
        parts.append('')
        parts.append(f'Full regression report: see `regressions-jsoniq` in [artifacts]({artifacts_url}).')
        parts.append('')
    parts.append(jsoniq_table_md)
    parts.append('')
    parts.append('</details>')
    parts.append('')
    parts.append(f'[Download detailed test results]({artifacts_url})')
    return '\n'.join(parts)


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument('--jsoniq', required=True, help='Path to JSONiq artifact directory')
    parser.add_argument('--xquery', required=True, help='Path to XQuery artifact directory')
    parser.add_argument('--jsoniq-regressions', help='Path to JSONiq regression artifact directory')
    parser.add_argument('--xquery-regressions', help='Path to XQuery regression artifact directory')
    parser.add_argument('--run-id', required=True, help='GitHub Actions run ID')
    parser.add_argument('--baseline-run-id', help='GitHub Actions baseline run ID')
    parser.add_argument('--repo-owner', required=True, help='Repository owner')
    parser.add_argument('--repo-name', required=True, help='Repository name')
    args = parser.parse_args()

    body = build_comment(
        args.jsoniq,
        args.xquery,
        args.run_id,
        args.baseline_run_id,
        args.repo_owner,
        args.repo_name,
        args.jsoniq_regressions,
        args.xquery_regressions,
    )
    print(body)


if __name__ == '__main__':
    main()
